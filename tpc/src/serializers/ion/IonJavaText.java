package serializers.ion;

import java.io.OutputStream;

import serializers.JavaBuiltIn;
import serializers.SerClass;
import serializers.SerFeatures;
import serializers.SerFormat;
import serializers.SerGraph;
import serializers.TestGroups;
import software.amazon.ion.IonReader;
import software.amazon.ion.IonSystem;
import software.amazon.ion.IonWriter;
import software.amazon.ion.system.IonSystemBuilder;

/**
 * Driver that uses the ion-java reference Amazon Ion implementation in Java to
 * manually serialize in the text Ion format.
 */
public class IonJavaText {
    private static final IonSystem SYSTEM = IonSystemBuilder.standard().build();
    
    public static void register(TestGroups groups) {
        groups.media.add(JavaBuiltIn.mediaTransformer,
            IonJavaBase.INSTANCE.new ManualTreeSerializer("ion/ion-java/text") {

                @Override
                IonReader createReader(byte[] array) {
                    return SYSTEM.newReader(array);
                }

                @Override
                IonWriter createWriter(OutputStream out) {
                    return SYSTEM.newTextWriter(out);
                }
            
            },
            new SerFeatures(
                SerFormat.MISC, // text, JSON superset
                SerGraph.FLAT_TREE,
                SerClass.MANUAL_OPT,
                ""));
    }
}
