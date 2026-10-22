package com.example.data.vocabulary

/**
 * Rich vocabulary repository containing thematic word lists for all 10 worlds,
 * organized by world and length for puzzle generation across 500 levels.
 */
object ThemedWordBank {

    val NATURE_WORDS = listOf(
        "OAK", "ELM", "PINE", "LEAF", "ROOT", "BARK", "TWIG", "SOIL", "RAIN", "MOSS",
        "SEED", "FERN", "STEM", "ROSE", "LILY", "DEW", "BUSH", "WOOD", "POND", "RIVER",
        "FOREST", "STREAM", "VALLEY", "CANYON", "FLOWER", "BLOSSOM", "PETAL", "BRANCH",
        "SPRING", "AUTUMN", "SUMMER", "WINTER", "FOLIAGE", "MEADOW", "JUNGLE", "GARDEN",
        "SUNSHINE", "BREEZE", "MOUNTAIN", "WATERFALL", "EVERGREEN", "WOODLAND", "WILDLIFE",
        "WILDERNESS", "RAINFOREST", "BOTANICAL", "NATURE", "CANOPY", "SAPLING", "ORCHARD"
    )

    val OCEAN_WORDS = listOf(
        "SEA", "BAY", "FIN", "TIDE", "WAVE", "SURF", "REEF", "SAND", "KELP", "CLAM",
        "CRAB", "FISH", "SEAL", "DOCK", "COVE", "ISLE", "GULL", "BOAT", "MAST", "SAIL",
        "CORAL", "SHARK", "WHALE", "SHELL", "OTTER", "BEACH", "OCEAN", "ABYSS", "MARIN",
        "DOLPHIN", "STARFISH", "OCTOPUS", "LOBSTER", "CURRENT", "NAUTICAL", "SEAHORSE",
        "ANEMONE", "PELICAN", "SUBMARINE", "MARITIME", "TSUNAMI", "AQUARIUM", "JELLYFISH",
        "BARRACUDA", "UNDERWATER", "DEEPSEA", "SEASHORE", "CRUSTACEAN", "PLANKTON"
    )

    val SPACE_WORDS = listOf(
        "SUN", "MOON", "MARS", "STAR", "NOVA", "RING", "VOID", "BEAM", "DUST", "GLOW",
        "ORBIT", "COMET", "SOLAR", "LUNAR", "EARTH", "VENUS", "PLUTO", "TITAN", "ASTRO",
        "ROCKET", "PLANET", "METEOR", "PULSAR", "COSMOS", "GALAXY", "NEBULA", "AURORA",
        "ZENITH", "ECLIPSE", "GRAVITY", "JUPITER", "SATURN", "URANUS", "NEPTUNE", "SHUTTLE",
        "UNIVERSE", "ASTRONOMY", "SUPERNOVA", "TELESCOPE", "SPACESHIP", "CONSTELLATION",
        "INTERSTELLAR", "ATMOSPHERE", "ASTRONAUT", "SATELLITE", "COSMOLOGY", "LIGHTYEAR"
    )

    val CANDY_WORDS = listOf(
        "PIE", "GUM", "JAM", "DIP", "CAKE", "TART", "ICING", "SUGAR", "SWEET", "FUDGE",
        "JELLY", "CREAM", "CHIPS", "CANDY", "WAFER", "DONUT", "SYRUP", "HONEY", "CRISP",
        "COOKIE", "TOFFEE", "PASTRY", "SUNDAE", "MOUSSE", "NOUGAT", "CARAMEL", "BONBON",
        "CHOCOLATE", "LOLLIPOP", "MARSHMALLOW", "GUMMYBEAR", "PEPPERMINT", "CONFECTION",
        "BUTTERSCOTCH", "FROSTING", "LIQUORICE", "CUPCAKE", "GUMBALL", "JELLYBEAN", "POPSICLE"
    )

    val ANIMAL_WORDS = listOf(
        "FOX", "OWL", "BAT", "ELK", "APE", "LION", "WOLF", "BEAR", "DEER", "HARE",
        "LYNX", "PUMA", "SEAL", "SWAN", "HAWK", "EAGLE", "TIGER", "PANDA", "ZEBRA",
        "HORSE", "KOALA", "OTTER", "BISON", "CAMEL", "LLAMA", "MOOSE", "SLOTH", "BADGER",
        "GIRAFFE", "DOLPHIN", "PENGUIN", "CHEETAH", "GORILLA", "LEOPARD", "KANGAROO",
        "ELEPHANT", "FLAMINGO", "ALLIGATOR", "CROCODILE", "CHAMELEON", "HEDGEHOG", "HIPPOPOTAMUS"
    )

    val FOOD_WORDS = listOf(
        "EGG", "HAM", "FIG", "NUT", "RICE", "BREAD", "SOUP", "STEAK", "SALAD", "TOAST",
        "TACO", "PASTA", "PIZZA", "SUSHI", "CURRY", "BAGEL", "APPLE", "BERRY", "MANGO",
        "MELON", "LEMON", "PEACH", "OLIVE", "CHEESE", "BURGER", "NOODLE", "OMELET",
        "PANCAKE", "WAFFLE", "RISOTTO", "AVOCADO", "SANDWICH", "BURRITO", "SPAGHETTI",
        "BARBECUE", "CROISSANT", "CASSEROLE", "DUMPLING", "APPETIZER", "GUACAMOLE", "SMOOTHIE"
    )

    val TRAVEL_WORDS = listOf(
        "MAP", "BUS", "VAN", "JET", "ROAD", "TRIP", "TOUR", "PACK", "PATH", "CAMP",
        "HIKE", "SUIT", "RAIL", "PORT", "TENT", "TRAIN", "FLIGHT", "CRUISE", "HOTEL",
        "RESORT", "TICKET", "VOYAGE", "SAFARI", "ISLAND", "COMPASS", "JOURNEY", "AIRPORT",
        "STATION", "TOURIST", "PASSPORT", "EXPEDITION", "ADVENTURE", "LUGGAGE", "SOUVENIR",
        "DESTINATION", "ITINERARY", "BACKPACK", "WANDERLUST", "MONUMENT", "LANDMARK", "EXPLORATION"
    )

    val SCIENCE_WORDS = listOf(
        "ATOM", "CELL", "GENE", "MASS", "WAVE", "BEAM", "CODE", "DATA", "LASER", "OPTIC",
        "ROBOT", "SOLAR", "CIRCUIT", "ENERGY", "FUSION", "PHOTON", "PROTON", "NEURON",
        "PLASMA", "QUANTUM", "ELECTRON", "GRAVITY", "ORGANISM", "MOLECULE", "GENETICS",
        "ELEMENT", "CHEMICAL", "PHYSICS", "BIOLOGY", "COMPUTER", "RESEARCH", "LABORATORY",
        "MICROSCOPE", "TELESCOPE", "HYPOTHESIS", "EXPERIMENT", "TECHNOLOGY", "NANOTECH"
    )

    val MYSTERY_WORDS = listOf(
        "RUNE", "CLUE", "MYTH", "MASK", "TOMB", "MAZE", "CRYPT", "SPELL", "GHOST", "SHADOW",
        "VAULT", "SECRET", "ENIGMA", "TEMPLE", "SCROLL", "RIDDLE", "POTION", "SYMBOL",
        "PHANTOM", "ILLUSION", "CRYSTAL", "LABYRINTH", "RELIC", "MIRACLE", "ALCHEMY",
        "SORCERY", "CHAMBER", "SPELLBOUND", "DISCOVERY", "TREASURE", "PROPHECY", "ANCIENT"
    )

    val MASTER_WORDS = listOf(
        "GOLD", "GEM", "CROWN", "EPIC", "HERO", "GLORY", "POWER", "MAGIC", "ROYAL", "VALOR",
        "SHIELD", "SWORD", "THRONE", "KNIGHT", "LEGEND", "PALACE", "CHAMPION", "VICTORY",
        "TRIUMPH", "MAJESTY", "WARRIOR", "FORTUNE", "IMMORTAL", "BRILLIANT", "TREASURE",
        "DYNASTY", "PRESTIGE", "CONQUEST", "MAGNIFICENT", "SUPERB", "DOMINANCE", "SOVEREIGN"
    )

    val UNIVERSAL_WORDS = listOf(
        "JOY", "SUN", "SKY", "AIR", "WIN", "WARM", "COOL", "BRIGHT", "LUCKY", "PEACE",
        "HAPPY", "SMILE", "LAUGH", "BRAVE", "SMART", "SHINE", "SPARK", "DREAM", "LIGHT",
        "WONDER", "HARMONY", "CHEERFUL", "RADIANT", "SPLENDID", "DELIGHT", "FRIEND"
    )

    /**
     * Get candidate words for a specific world and difficulty tier.
     */
    fun getWordsForWorld(worldId: Int): List<String> {
        val baseList = when (worldId) {
            1 -> NATURE_WORDS
            2 -> OCEAN_WORDS
            3 -> SPACE_WORDS
            4 -> CANDY_WORDS
            5 -> ANIMAL_WORDS
            6 -> FOOD_WORDS
            7 -> TRAVEL_WORDS
            8 -> SCIENCE_WORDS
            9 -> MYSTERY_WORDS
            10 -> MASTER_WORDS
            else -> NATURE_WORDS
        }
        return (baseList + UNIVERSAL_WORDS).distinct()
    }
}
