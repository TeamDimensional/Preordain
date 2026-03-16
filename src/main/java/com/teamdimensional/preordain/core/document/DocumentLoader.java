package com.teamdimensional.preordain.core.document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.teamdimensional.preordain.Preordain;
import com.teamdimensional.preordain.core.function.PreordainFunction;
import com.teamdimensional.preordain.core.function.PreordainFunctionDeserializer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DocumentLoader {
    public final Map<String, PreordainDocument> documents = new Object2ObjectOpenHashMap<>();
    private final String DOCUMENT_PATH = "preordain";
    private final Gson gson = new GsonBuilder()
        .registerTypeAdapter(PreordainFunction.class, new PreordainFunctionDeserializer())
        .create();
    private File instanceDir = null;
    public final DocumentItemLinker linker = new DocumentItemLinker();

    private void loadFile(String name, InputStream ifs) {
        Preordain.LOGGER.debug("Found Preordain document at {}", name);
        try {
            Reader reader = new InputStreamReader(ifs, StandardCharsets.UTF_8);
            PreordainDocument doc = gson.fromJson(reader, PreordainDocument.class);
            documents.put(doc.getKey(), doc);
        } catch (IllegalArgumentException e) {
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

    public void load() {
        documents.clear();
        linker.links.clear();

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

        Preordain.LOGGER.info("Loaded " + documents.size() + " files");
    }

    public void init() {
        for (Map.Entry<String, PreordainDocument> entry : documents.entrySet()) {
            try {
                entry.getValue().initialize(this);
            } catch (IllegalArgumentException e) {
                Preordain.LOGGER.error("Error while initializing file {}: {}", entry.getKey(), e.getMessage());
                entry.getValue().markInitialized();
            }
        }
    }
}
