package tmc.BetterProtected.svc;

import tmc.BetterProtected.domain.ProtectedChunkKey;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransformationService {
    public static final String FILE_REGEX = ".*\\\\(-*\\d{0,5}).(-*\\d{0,5})\\.yml";
    private final Pattern pattern;

    public TransformationService() {
        pattern = Pattern.compile(FILE_REGEX);
    }

    public ProtectedChunkKey parseChunkKeyFromFileName(String fileName) {
        Matcher matcher = pattern.matcher(fileName);

        if (matcher.matches()) {
            Integer x = Integer.parseInt(matcher.group(1));
            Integer z = Integer.parseInt(matcher.group(2));

            return new ProtectedChunkKey(x, z);
        }

        return null;
    }
}
