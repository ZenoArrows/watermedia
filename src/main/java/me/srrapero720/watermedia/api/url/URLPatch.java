package me.srrapero720.watermedia.api.url;

import me.srrapero720.watermedia.api.url.patch.*;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public abstract class URLPatch {
    public static URLPatch[] URL_PATCHERS = null;

    public static boolean init() {
        URL_PATCHERS = new URLPatch[] {
                new YoutubePatch(),
                new TwitchPatch(),
                new KickPatch(),
                new DrivePatch(),
                new TwitterPatch(),
        };
        return true;
    }
    public abstract boolean isValid(@NotNull URL url);
    public String build(@NotNull URL url) throws PatchingUrlException {
        if (!isValid(url)) throw new PatchingUrlException(url, new IllegalArgumentException("Attempt to build a invalid URL in a invalid Compat"));
        return null;
    }

    public static final class PatchingUrlException extends Exception {
        public PatchingUrlException(String url, Throwable t) { super("Failed to patch URL " + url, t); }
        public PatchingUrlException(URL url, Throwable t) { super("Failed to patch URL " + url.toString(), t); }
    }
}
