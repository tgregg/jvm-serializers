package serializers.ion;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import data.media.Image;
import data.media.Media;
import data.media.MediaContent;
import serializers.Serializer;
import software.amazon.ion.IonReader;
import software.amazon.ion.IonType;
import software.amazon.ion.IonWriter;

public class IonJavaBase {
    
    static IonJavaBase INSTANCE = new IonJavaBase();
    
    private IonJavaBase(){}
    
    abstract class ManualTreeSerializer extends Serializer<MediaContent> {

        final String name;
        
        ManualTreeSerializer(String name) {
            this.name = name;
        }
        
        @Override
        public MediaContent deserialize(byte[] array) throws Exception {
            return readMediaContent(createReader(array));
        }

        @Override
        public byte[] serialize(MediaContent content) throws Exception {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (IonWriter writer = createWriter(out)) {
                writeMediaContent(content, writer);
            }
            return out.toByteArray();
        }

        @Override
        public String getName() {
            return name;
        }
        
        abstract IonReader createReader(byte[] array);
        
        abstract IonWriter createWriter(OutputStream out);

    }
    
    static MediaContent readMediaContent(IonReader reader) {
        MediaContent content = new MediaContent();
        reader.next();
        reader.stepIn(); {
            while (reader.next() != null) {
                switch (reader.getFieldName()) {
                case "media":
                    content.media = readMedia(reader);
                    break;
                case "images":
                    List<Image> images = new ArrayList<>();
                    reader.stepIn(); {
                        while(reader.next() != null) {
                            images.add(readImage(reader));
                        }
                    }
                    reader.stepOut();
                    content.images = images;
                    break;
                default:
                    break;
                }
            }
        }
        reader.stepOut();
        return content;
    }
    
    static Media readMedia(IonReader reader) {
        Media media = new Media();
        reader.stepIn(); {
            while (reader.next() != null) {
                switch (reader.getFieldName()) {
                case "hasBitrate":
                    media.hasBitrate = reader.booleanValue();
                    break;
                case "bitrate":
                    media.bitrate = reader.intValue();
                    break;
                case "height":
                    media.height = reader.intValue();
                    break;
                case "width":
                    media.width = reader.intValue();
                    break;
                case "duration":
                    media.duration = reader.longValue();
                    break;
                case "size":
                    media.size = reader.longValue();
                    break;
                case "copyright":
                    media.copyright = reader.stringValue();
                    break;
                case "format":
                    media.format = reader.stringValue();
                    break;
                case "title":
                    media.title = reader.stringValue();
                    break;
                case "uri":
                    media.uri = reader.stringValue();
                    break;
                case "persons":
                    List<String> persons = new ArrayList<>();
                    reader.stepIn(); {
                        while (reader.next() != null) {
                            persons.add(reader.stringValue());
                        }
                    }
                    reader.stepOut();
                    media.persons = persons;
                    break;
                case "player":
                    media.player = Media.Player.valueOf(reader.stringValue());
                    break;
                default:
                    break;
                }
            }
        }
        reader.stepOut();
        return media;
    }
    
    static Image readImage(IonReader reader) {
        Image image = new Image();
        reader.stepIn(); {
            while (reader.next() != null) {
                switch (reader.getFieldName()) {
                case "height":
                    image.height = reader.intValue();
                    break;
                case "width":
                    image.width = reader.intValue();
                    break;
                case "title":
                    image.title = reader.stringValue();
                    break;
                case "uri":
                    image.uri = reader.stringValue();
                    break;
                case "size":
                    image.size = Image.Size.valueOf(reader.stringValue());
                    break;
                default:
                    break;
                }
            }
        }
        reader.stepOut();
        return image;
    }
    
    static void writeMediaContent(MediaContent content, IonWriter writer) throws IOException {
        writer.stepIn(IonType.STRUCT); {
            writer.setFieldName("media");
            writeMedia(content.media, writer);
            writer.setFieldName("images");
            writer.stepIn(IonType.LIST); {
                for (Image image : content.images) {
                    writeImage(image, writer);
                }
            }
            writer.stepOut();
        }
        writer.stepOut();
    }
    
    static void writeMedia(Media media, IonWriter writer) throws IOException {
        writer.stepIn(IonType.STRUCT); {
            writer.setFieldName("hasBitrate");
            writer.writeBool(media.hasBitrate);
            writer.setFieldName("bitrate");
            writer.writeInt(media.bitrate);
            writer.setFieldName("height");
            writer.writeInt(media.height);
            writer.setFieldName("width");
            writer.writeInt(media.width);
            writer.setFieldName("duration");
            writer.writeInt(media.duration);
            writer.setFieldName("size");
            writer.writeInt(media.size);
            writer.setFieldName("copyright");
            writer.writeString(media.copyright);
            writer.setFieldName("format");
            writer.writeString(media.format);
            writer.setFieldName("title");
            writer.writeString(media.title);
            writer.setFieldName("uri");
            writer.writeString(media.uri);
            writer.setFieldName("persons");
            writer.stepIn(IonType.LIST); {
                for (String person : media.persons) {
                    writer.writeString(person);
                }
            }
            writer.stepOut();
            writer.setFieldName("player");
            writer.writeString(media.player.toString());
        }
        writer.stepOut();
    }
    
    static void writeImage(Image image, IonWriter writer) throws IOException {
        writer.stepIn(IonType.STRUCT); {
            writer.setFieldName("height");
            writer.writeInt(image.height);
            writer.setFieldName("width");
            writer.writeInt(image.width);
            writer.setFieldName("title");
            writer.writeString(image.title);
            writer.setFieldName("uri");
            writer.writeString(image.uri);
            writer.setFieldName("size");
            writer.writeString(image.size.toString());
        }
        writer.stepOut();
    }
    
}
