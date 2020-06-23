import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static Map<String, List<String>> map;

    static List<String> results = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        List<String> items = Files.lines(Paths.get("Cities.txt")).collect(Collectors.toList());

        map = new HashMap<>();
        for (String item : items) {
            createMap(item, new ArrayList<>(items));
        }

        List<String> noEntry = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            if (entry.getValue().size() == 0) {
                noEntry.add(entry.getKey());
            }
        }
        noEntry.forEach(item -> map.remove(item));

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            List<String> variants = entry.getValue();
            locate(variants, entry.getKey(), entry.getKey());
        }

        Map<Integer, String> res = new HashMap<>();
        results.forEach(s -> {
            int size = s.split(" -> ").length;
            res.put(size, s);
        });
        int max = res.keySet().stream().max(Integer::compareTo).orElse(-1);
        System.out.println("Max chain " + res.get(max));
    }

    static void locate(List<String> list, String var, String prev) {
        for (String variant : list) {
            if (var.contains(variant)) {
                results.add(var);
                continue;
            }
            List<String> innerVar = map.get(variant);
            if (innerVar != null && !innerVar.isEmpty()) {
                locate(innerVar, var + " -> " + variant, var);
            } else {
                results.add(var + " -> " + variant);
                var = prev;
            }
        }
    }

    static void createMap(String item, List<String> items) {
        List<String> associate = new ArrayList<>();
        String lastChar = item.substring(item.length() - 1).toLowerCase();
        for (String itemInner : items) {
            if (!item.equals(itemInner)) {
                String firstChar = itemInner.substring(0, 1).toLowerCase();
                if (lastChar.equals(firstChar))
                    associate.add(itemInner);
            }
        }
        map.put(item, associate);
    }
}