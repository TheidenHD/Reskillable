package codersafterdark.reskillable.api.transmutations;

import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;

public class TransmutationRegistry {

    private static Block[] wool = new Block[] {
            Blocks.WHITE_WOOL, Blocks.ORANGE_WOOL, Blocks.MAGENTA_WOOL,
            Blocks.LIGHT_BLUE_WOOL, Blocks.YELLOW_WOOL, Blocks.LIME_WOOL,
            Blocks.PINK_WOOL, Blocks.GRAY_WOOL, Blocks.LIGHT_GRAY_WOOL,
            Blocks.CYAN_WOOL, Blocks.PURPLE_WOOL, Blocks.BLUE_WOOL,
            Blocks.BROWN_WOOL, Blocks.GREEN_WOOL, Blocks.RED_WOOL, Blocks.BLACK_WOOL
    };

    private static Block[] carpet = new Block[] {
      Blocks.WHITE_CARPET, Blocks.ORANGE_CARPET, Blocks.MAGENTA_CARPET,
      Blocks.LIGHT_BLUE_CARPET, Blocks.YELLOW_CARPET, Blocks.LIME_CARPET,
      Blocks.PINK_CARPET, Blocks.GRAY_CARPET, Blocks.LIGHT_GRAY_CARPET,
      Blocks.CYAN_CARPET, Blocks.PURPLE_CARPET, Blocks.BLUE_CARPET,
            Blocks.BROWN_CARPET, Blocks.GREEN_CARPET, Blocks.RED_CARPET, Blocks.BLACK_CARPET
    };

    private static Block[] stained_glass = new Block[] {
            Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS,
            Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS,
            Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS,
            Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS,
            Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS
    };

    private static Block[] stained_glass_pane = new Block[] {
            Blocks.WHITE_STAINED_GLASS_PANE, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.MAGENTA_STAINED_GLASS_PANE,
            Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.LIME_STAINED_GLASS_PANE,
            Blocks.PINK_STAINED_GLASS_PANE, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE,
            Blocks.CYAN_STAINED_GLASS_PANE, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.BLUE_STAINED_GLASS_PANE,
            Blocks.BROWN_STAINED_GLASS_PANE, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.RED_STAINED_GLASS_PANE, Blocks.BLACK_STAINED_GLASS_PANE
    };

    private static Block[] concrete = new Block[] {
            Blocks.WHITE_CONCRETE, Blocks.ORANGE_CONCRETE, Blocks.MAGENTA_CONCRETE,
            Blocks.LIGHT_GRAY_CONCRETE, Blocks.YELLOW_CONCRETE, Blocks.LIME_CONCRETE,
            Blocks.PINK_CONCRETE, Blocks.GRAY_CONCRETE, Blocks.LIGHT_GRAY_CONCRETE,
            Blocks.CYAN_CONCRETE, Blocks.PURPLE_CONCRETE, Blocks.BLUE_CONCRETE,
            Blocks.BROWN_CONCRETE, Blocks.GREEN_CONCRETE, Blocks.RED_CONCRETE, Blocks.BLACK_CONCRETE
    };

    private static Block[] glazed_terracotta = new Block[] {
            Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_STAINED_GLASS,
            Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA,
            Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA,
            Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA,
            Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA
    };

    private static Map<Item, Map<BlockState, BlockState>> reagentStateMap = new HashMap<>();

    public static Map<Item, Map<BlockState, BlockState>> getReagentStateMap() {
        return reagentStateMap;
    }

    public static Map<BlockState, BlockState> getStateMapByReagent(Item item) {
        return reagentStateMap.get(item);
    }

    public static void initDefaultMap() {
        Item item = Items.CHORUS_FRUIT;

        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.MELON, Blocks.PUMPKIN);

        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.MAGMA_BLOCK, Blocks.ICE);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.ICE, Blocks.MAGMA_BLOCK);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.STONE, Blocks.END_STONE);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.END_STONE, Blocks.STONE);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.CLAY, Blocks.TERRACOTTA);
        TransmutationRegistry.addEntryToReagentByBlockDefaultState(item, Blocks.TERRACOTTA, Blocks.CLAY);

        TransmutationRegistry.addEntryToReagent(item, Blocks.WET_SPONGE.defaultBlockState(), Blocks.SPONGE.defaultBlockState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.SPONGE.defaultBlockState(), Blocks.WET_SPONGE.defaultBlockState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.PRISMARINE.defaultBlockState(), Blocks.PRISMARINE_BRICKS.defaultBlockState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.PRISMARINE_BRICKS.defaultBlockState(), Blocks.DARK_PRISMARINE.defaultBlockState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.DARK_PRISMARINE.defaultBlockState(), Blocks.PRISMARINE.defaultBlockState());

        for (Direction face : Direction.Plane.HORIZONTAL) {
            TransmutationRegistry.addEntryToReagent(item, Blocks.PUMPKIN.defaultBlockState().setValue(BlockStateProperties.FACING, face), Blocks.MELON.defaultBlockState());
        }

        TransmutationRegistry.addEntryToReagent(item, Blocks.OAK_SAPLING.defaultBlockState(), Blocks.SPRUCE_SAPLING.defaultBlockState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.SPRUCE_SAPLING.defaultBlockState(), Blocks.BIRCH_SAPLING.defaultBlockState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.BIRCH_SAPLING.defaultBlockState(), Blocks.JUNGLE_SAPLING.defaultBlockState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.JUNGLE_SAPLING.defaultBlockState(), Blocks.ACACIA_SAPLING.defaultBlockState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.ACACIA_SAPLING.defaultBlockState(), Blocks.CHERRY_SAPLING.defaultBlockState());
        TransmutationRegistry.addEntryToReagent(item, Blocks.CHERRY_SAPLING.defaultBlockState(), Blocks.DARK_OAK_SAPLING.defaultBlockState());

        for (int i = 0; i < 16; i++) {
            if (i == 15) {
                TransmutationRegistry.addEntryToReagent(item, wool[i].defaultBlockState(), wool[0].defaultBlockState());
                TransmutationRegistry.addEntryToReagent(item, carpet[i].defaultBlockState(), carpet[0].defaultBlockState());
                TransmutationRegistry.addEntryToReagent(item, stained_glass[i].defaultBlockState(), stained_glass[0].defaultBlockState());
                TransmutationRegistry.addEntryToReagent(item, stained_glass_pane[i].defaultBlockState(), stained_glass_pane[0].defaultBlockState());
                TransmutationRegistry.addEntryToReagent(item, concrete[i].defaultBlockState(), concrete[0].defaultBlockState());
                TransmutationRegistry.addEntryToReagent(item, glazed_terracotta[i].defaultBlockState(), glazed_terracotta[0].defaultBlockState());
                break;
            }
            TransmutationRegistry.addEntryToReagent(item, wool[i].defaultBlockState(), wool[i++].defaultBlockState());
            TransmutationRegistry.addEntryToReagent(item, carpet[i].defaultBlockState(), carpet[i++].defaultBlockState());
            TransmutationRegistry.addEntryToReagent(item, stained_glass[i].defaultBlockState(), stained_glass[i++].defaultBlockState());
            TransmutationRegistry.addEntryToReagent(item, stained_glass_pane[i].defaultBlockState(), stained_glass_pane[i++].defaultBlockState());
            TransmutationRegistry.addEntryToReagent(item, concrete[i].defaultBlockState(), concrete[0].defaultBlockState());
            TransmutationRegistry.addEntryToReagent(item, glazed_terracotta[i].defaultBlockState(), glazed_terracotta[0].defaultBlockState());
        }

    }

    public static boolean doesStateMapContainKeyState(BlockState state, Map<BlockState, BlockState> stateMap) {
        return stateMap.containsKey(state);
    }

    public static boolean doesReagentStateMapContainReagentItem(Item item) {
        return reagentStateMap.containsKey(item);
    }

    ////////////////////////
    /// Addition Methods ///
    ////////////////////////

    public static void addEntryToReagentByBlockDefaultState(Item item, Block block1, Block block2) {
        if (reagentStateMap.containsKey(item)) {
            Map<BlockState, BlockState> map = reagentStateMap.get(item);
            map.put(block1.defaultBlockState(), block2.defaultBlockState());
        } else {
            Map<BlockState, BlockState> stateMap = new HashMap<>();
            stateMap.put(block1.defaultBlockState(), block2.defaultBlockState());
            reagentStateMap.put(item, stateMap);
        }
    }

    public static void addEntryToReagent(Item item, BlockState state1, BlockState state2) {
        if (reagentStateMap.containsKey(item)) {
            Map<BlockState, BlockState> map = reagentStateMap.get(item);
            map.put(state1, state2);
        } else {
            Map<BlockState, BlockState> stateMap = new HashMap<>();
            stateMap.put(state1, state2);
            reagentStateMap.put(item, stateMap);
        }
    }

    public static void addEntriesToReagent(Item item, Map<BlockState, BlockState> map) {
        if (reagentStateMap.containsKey(item)) {
            Map<BlockState, BlockState> mapster = reagentStateMap.get(item);
            for (Map.Entry<BlockState, BlockState> entry : map.entrySet()) {
                mapster.put(entry.getKey(), entry.getValue());
            }
        } else {
            Map<BlockState, BlockState> stateMap = new HashMap<>();
            for (Map.Entry<BlockState, BlockState> entry : map.entrySet()) {
                stateMap.put(entry.getKey(), entry.getValue());
            }
            reagentStateMap.put(item, stateMap);
        }
    }

    public static void addEntryReagentAgnostic(BlockState state1, BlockState state2) {
        for (Item key : reagentStateMap.keySet()) {
            Map<BlockState, BlockState> map = reagentStateMap.get(key);
            map.put(state1, state2);
        }
    }

    public static void addEntriesReagentAgnostic(Map<BlockState, BlockState> mapster) {
        for (Item key : reagentStateMap.keySet()) {
            Map<BlockState, BlockState> map = reagentStateMap.get(key);
            for (Map.Entry<BlockState, BlockState> entry : mapster.entrySet()) {
                map.put(entry.getKey(), entry.getValue());
            }
        }
    }


    ///////////////////////
    /// Removal Methods ///
    ///////////////////////

    public static void removeStartStateFromReagentAgnostic(BlockState state) {
        for (Item reagent : reagentStateMap.keySet()) {
            reagentStateMap.get(reagent).remove(state);
        }
    }

    public static void removeEndStateFromReagentAgnostic(BlockState state) {
        for (Item reagent : reagentStateMap.keySet()) {
            for (Map.Entry<BlockState, BlockState> stateEntry : reagentStateMap.get(reagent).entrySet()) {
                if (stateEntry.getValue() == state) {
                    reagentStateMap.get(reagent).remove(stateEntry.getKey());
                }
            }
        }
    }

    public static void removeStartStateFromReagent(Item reagent, BlockState state) {
        reagentStateMap.get(reagent).remove(state);
    }

    public static void removeEndStateFromReagent(Item reagent, BlockState state) {
        for (Map.Entry<BlockState, BlockState> stateEntry : reagentStateMap.get(reagent).entrySet()) {
            if (stateEntry.getValue() == state) {
                reagentStateMap.get(reagent).remove(stateEntry.getKey());
            }
        }
    }


    /////////////////////
    /// Clear Methods ///
    /////////////////////
    public static void clearMapOfReagent(Item stack) {
        reagentStateMap.remove(stack);
    }

    public static void clearReagentOfEntries(Item stack) {
        reagentStateMap.get(stack).clear();
    }

    ////////////////////////////////////////////////////////////////
    //         Clear's the entire Transmutation Map               //
    // Do Not Use This Unless You Have A Very Specific Reasoning! //
    ////////////////////////////////////////////////////////////////
    public static void clearReagentMap() {
        reagentStateMap.clear();
    }
}
