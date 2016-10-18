package fr.mpiffault.trait;

public class Utils {
    public static boolean isDebugMode() {
        return "true".equals(System.getProperty("GRAPH_DEBUG"));
    }
}
