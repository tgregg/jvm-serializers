package serializers.ion;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;

import serializers.JavaBuiltIn;
import serializers.SerClass;
import serializers.SerFeatures;
import serializers.SerFormat;
import serializers.SerGraph;
import serializers.TestGroups;
import software.amazon.ion.IonCatalog;
import software.amazon.ion.IonReader;
import software.amazon.ion.IonSystem;
import software.amazon.ion.IonWriter;
import software.amazon.ion.SymbolTable;
import software.amazon.ion.system.IonSystemBuilder;
import software.amazon.ion.system.SimpleCatalog;

/**
 * Driver that uses the ion-java reference Amazon Ion implementation in Java to
 * manually serialize in the binary Ion format, using pre-configured shared symbol
 * tables.
 */
public class IonJavaBinarySymbolTables {
    
    private static final SymbolTable MEDIACONTENT_SYMBOLS;
    private static final IonSystem SYSTEM; 
    static {
        IonSystem system = IonSystemBuilder.standard().build();
        Iterator<String> symbols = Arrays.asList("media",
                                                 "images",
                                                 "hasBitrate",
                                                 "bitrate",
                                                 "height",
                                                 "width",
                                                 "duration",
                                                 "size",
                                                 "copyright",
                                                 "format",
                                                 "title",
                                                 "uri",
                                                 "persons",
                                                 "player").iterator();
        MEDIACONTENT_SYMBOLS = system.newSharedSymbolTable("mc", 1, symbols);
        SimpleCatalog catalog = new SimpleCatalog();
        catalog.putTable(MEDIACONTENT_SYMBOLS);
        SYSTEM = IonSystemBuilder.standard().withCatalog(catalog).build();
    }
    
    public static void register(TestGroups groups) {
        groups.media.add(JavaBuiltIn.mediaTransformer,
            IonJavaBase.INSTANCE.new ManualTreeSerializer("ion/ion-java/binary-symtabs") {

                @Override
                IonReader createReader(byte[] array) {
                    return SYSTEM.newReader(array);
                }

                @Override
                IonWriter createWriter(OutputStream out) {
                    return SYSTEM.newBinaryWriter(out, MEDIACONTENT_SYMBOLS);
                }
            
            },
            new SerFeatures(
                SerFormat.BIN_CROSSLANG,
                SerGraph.FLAT_TREE,
                SerClass.MANUAL_OPT,
                ""));
    }
    
}
