package fr.bafbi.qsky.utils;

import java.util.Random;

import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.ChunkGenerator;

public class VoidChunkGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {

        ChunkData chunk = createChunkData(world);

        for (int x = 0; x < 16; x++) {

            for (int y = 0; y < 256; y++) {

                for (int z = 0; z < 16; z++) {

                    biome.setBiome(x, y, z, Biome.PLAINS);
                }
            }
        }

        return chunk;

        
    }
    
}
