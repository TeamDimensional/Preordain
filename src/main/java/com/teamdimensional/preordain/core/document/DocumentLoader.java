package com.teamdimensional.preordain.core.document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.document.DocumentChecker.DocumentCheckingException;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.core.function.PreordainFunctionDeserializer;
import com.teamdimensional.preordain.library.RevertibleRegistry;
import com.teamdimensional.preordain.library.serialization.DataSerializers;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Nullable;

public class DocumentLoader {
    private static final String DOCUMENT_PATH = "preordain";
    public static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(PreordainFunction.class, new PreordainFunctionDeserializer())
        .registerTypeAdapter(ItemStack.class, new DataSerializers.ItemStackDeserializer())
        .registerTypeAdapter(AxisAlignedBB.class, new DataSerializers.AxisAlignedBBDeserializer())
        .registerTypeAdapter(IBlockState.class, new DataSerializers.BlockStateDeserializer())
        .registerTypeAdapter(BlockPos.class, new DataSerializers.BlockPosDeserializer())
        .create();

    private final RevertibleRegistry<Map<String, PreordainDocument>> documents
        = new RevertibleRegistry<>(Object2ObjectOpenHashMap::new);
    private File instanceDir = null;
    public final DocumentItemLinker linker = new DocumentItemLinker();

    private void loadFile(String name, InputStream ifs) {
        Preordain.LOGGER.debug("Found Preordain document at {}", name);
        try {
            Reader reader = new InputStreamReader(ifs, StandardCharsets.UTF_8);
            PreordainDocument doc = gson.fromJson(reader, PreordainDocument.class);
            documents.get().put(doc.getKey(), doc);
        } catch (JsonSyntaxException e) {
            Preordain.LOGGER.error("Error while loading file {}: {}", name, e.getMessage());
        }
    }

    private void loadFile(File file) {
        try (FileInputStream ifs = new FileInputStream(file)) {
            loadFile(file.toString(), ifs);
        } catch (IOException e) {
            Preordain.LOGGER.error("Preordain document {} unexpectedly disappeared, race condition?", file);
        }
    }

    public DocumentLoader(File instanceDir) {
        this.instanceDir = instanceDir;
    }

    public boolean load() {
        documents.beginTransaction();
        linker.links.beginTransaction();

        // load modded preordains
        List<ModContainer> mods = Loader.instance().getActiveModList();
        mods.forEach((mod) -> {
            String id = mod.getModId();
            CraftingHelper.findFiles(mod, String.format("assets/%s/%s", id, DOCUMENT_PATH), f -> true,
                (path, file) -> {
                    if (!file.getFileName().toString().endsWith(".json")) return true;

                    String fileStr = file.toString().replaceAll("\\\\", "/");
                    String assetPath = fileStr.substring(fileStr.indexOf("/assets"));
                    loadFile(fileStr, mod.getMod().getClass().getResourceAsStream(assetPath));
                    return true;
                }, false, true);
        });

        // then load custom preordains so they're later in the list
        File loadDir = new File(instanceDir, DOCUMENT_PATH);
        if (!loadDir.exists()) {
            if (!loadDir.mkdir()) {
                Preordain.LOGGER.error("Unable to create the local directory {}!", loadDir);
            }
        }

        if (!loadDir.isDirectory()) {
            Preordain.LOGGER.error("File {} exists and is not a directory. Custom Preordain Documents were not loaded.", loadDir.getAbsolutePath());
        } else {
            for (File file : Objects.requireNonNull(loadDir.listFiles())) {
                loadFile(file);
            }
        }

        Preordain.LOGGER.info("Loaded " + count() + " files");

        for (Map.Entry<String, PreordainDocument> entry : documents.get().entrySet()) {
            entry.getValue().loadLinks(this.linker);
        }

        DocumentChecker checker = new DocumentChecker(documents.get());
        try {
            checker.check();
        } catch (DocumentCheckingException e) {
            Preordain.LOGGER.error(
                "Error while validating documents at chain: {}. Error message: {}.",
                String.join(" -> ", e.dependencyChain.toArray(new String[]{})), e.getMessage());
            documents.undo();
            linker.links.undo();
            return false;
        }

        return true;
    }

    public @Nullable PreordainDocument getDocument(String name) {
        return documents.get().get(name);
    }

    public int count() {
        return documents.get().size();
    }
}
