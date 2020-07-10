package world.bentobox.bentobox.debug;

import org.bukkit.Bukkit;
import world.bentobox.bentobox.BentoBox;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DebugUtil {
    private static Thread mainThread;
    private static ExecutorService executor = Executors.newSingleThreadExecutor();
    private static BentoBox plugin;
    private static File debugLogFile;

    public static void init(BentoBox plugin) {
        DebugUtil.plugin = plugin;
        debugLogFile = new File(plugin.getDataFolder(), "debug.log");
        mainThread = Thread.currentThread();
    }

    public static void checkMainThread(String description) {
        if (isMainThread()) {
            logError(new Exception(description + " on main thread!"));
        }
    }

    public static boolean isMainThread() {
        return Thread.currentThread() == mainThread;
    }

    public static <T> T runOnMainThread(Callable<T> callable) {
        try {
            if (isMainThread()) {
                return callable.call();
            }
            return Bukkit.getScheduler().callSyncMethod(plugin, callable).get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void logError(Throwable throwable) {
        executor.execute(() -> {
            StringWriter stringWriter = new StringWriter();
            throwable.printStackTrace(new PrintWriter(stringWriter));
            String message = stringWriter.toString();

            String timestamp = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(OffsetDateTime.now());

            byte[] data = ("[" + timestamp + "] " + message).getBytes(StandardCharsets.UTF_8);
            try {
                Files.write(debugLogFile.toPath(), data, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
