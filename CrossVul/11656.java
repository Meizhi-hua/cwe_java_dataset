
package org.openhab.binding.sonos.internal;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringEscapeUtils;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;
@NonNullByDefault
public class SonosXMLParser {
    static final Logger LOGGER = LoggerFactory.getLogger(SonosXMLParser.class);
    private static final MessageFormat METADATA_FORMAT = new MessageFormat(
            "<DIDL-Lite xmlns:dc=\"http:
                    + "xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\" "
                    + "xmlns:r=\"urn:schemas-rinconnetworks-com:metadata-1-0/\" "
                    + "xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\">"
                    + "<item id=\"{0}\" parentID=\"{1}\" restricted=\"true\">" + "<dc:title>{2}</dc:title>"
                    + "<upnp:class>{3}</upnp:class>"
                    + "<desc id=\"cdudn\" nameSpace=\"urn:schemas-rinconnetworks-com:metadata-1-0/\">" + "{4}</desc>"
                    + "</item></DIDL-Lite>");
    private enum Element {
        TITLE,
        CLASS,
        ALBUM,
        ALBUM_ART_URI,
        CREATOR,
        RES,
        TRACK_NUMBER,
        RESMD,
        DESC
    }
    private enum CurrentElement {
        item,
        res,
        streamContent,
        albumArtURI,
        title,
        upnpClass,
        creator,
        album,
        albumArtist,
        desc
    }
    public static List<SonosAlarm> getAlarmsFromStringResult(String xml) {
        AlarmHandler handler = new AlarmHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            LOGGER.error("Could not parse Alarms from string '{}'", xml);
        } catch (SAXException s) {
            LOGGER.error("Could not parse Alarms from string '{}'", xml);
        }
        return handler.getAlarms();
    }
    public static List<SonosEntry> getEntriesFromString(String xml) {
        EntryHandler handler = new EntryHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            LOGGER.error("Could not parse Entries from string '{}'", xml);
        } catch (SAXException s) {
            LOGGER.error("Could not parse Entries from string '{}'", xml);
        }
        return handler.getArtists();
    }
    public static @Nullable SonosResourceMetaData getResourceMetaData(String xml) throws SAXException {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setFeature("http:
        ResourceMetaDataHandler handler = new ResourceMetaDataHandler();
        reader.setContentHandler(handler);
        try {
            reader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            LOGGER.error("Could not parse Resource MetaData from String '{}'", xml);
        } catch (SAXException s) {
            LOGGER.error("Could not parse Resource MetaData from string '{}'", xml);
        }
        return handler.getMetaData();
    }
    public static List<SonosZoneGroup> getZoneGroupFromXML(String xml) {
        ZoneGroupHandler handler = new ZoneGroupHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            LOGGER.error("Could not parse ZoneGroup from string '{}'", xml);
        } catch (SAXException s) {
            LOGGER.error("Could not parse ZoneGroup from string '{}'", xml);
        }
        return handler.getGroups();
    }
    public static List<String> getRadioTimeFromXML(String xml) {
        OpmlHandler handler = new OpmlHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            LOGGER.error("Could not parse RadioTime from string '{}'", xml);
        } catch (SAXException s) {
            LOGGER.error("Could not parse RadioTime from string '{}'", xml);
        }
        return handler.getTextFields();
    }
    public static Map<String, @Nullable String> getRenderingControlFromXML(String xml) {
        RenderingControlEventHandler handler = new RenderingControlEventHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            LOGGER.error("Could not parse Rendering Control from string '{}'", xml);
        } catch (SAXException s) {
            LOGGER.error("Could not parse Rendering Control from string '{}'", xml);
        }
        return handler.getChanges();
    }
    public static Map<String, @Nullable String> getAVTransportFromXML(String xml) {
        AVTransportEventHandler handler = new AVTransportEventHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            LOGGER.error("Could not parse AV Transport from string '{}'", xml);
        } catch (SAXException s) {
            LOGGER.error("Could not parse AV Transport from string '{}'", xml);
        }
        return handler.getChanges();
    }
    public static SonosMetaData getMetaDataFromXML(String xml) {
        MetaDataHandler handler = new MetaDataHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            LOGGER.error("Could not parse MetaData from string '{}'", xml);
        } catch (SAXException s) {
            LOGGER.error("Could not parse MetaData from string '{}'", xml);
        }
        return handler.getMetaData();
    }
    public static List<SonosMusicService> getMusicServicesFromXML(String xml) {
        MusicServiceHandler handler = new MusicServiceHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(handler);
            reader.parse(new InputSource(new StringReader(xml)));
        } catch (IOException e) {
            LOGGER.error("Could not parse music services from string '{}'", xml);
        } catch (SAXException s) {
            LOGGER.error("Could not parse music services from string '{}'", xml);
        }
        return handler.getServices();
    }
    private static class EntryHandler extends DefaultHandler {
        private static @Nullable List<String> ignore;
        private String id = "";
        private String parentId = "";
        private StringBuilder upnpClass = new StringBuilder();
        private StringBuilder res = new StringBuilder();
        private StringBuilder title = new StringBuilder();
        private StringBuilder album = new StringBuilder();
        private StringBuilder albumArtUri = new StringBuilder();
        private StringBuilder creator = new StringBuilder();
        private StringBuilder trackNumber = new StringBuilder();
        private StringBuilder desc = new StringBuilder();
        private @Nullable Element element;
        private List<SonosEntry> artists = new ArrayList<>();
        EntryHandler() {
        }
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            String name = qName == null ? "" : qName;
            switch (name) {
                case "container":
                case "item":
                    if (attributes != null) {
                        id = attributes.getValue("id");
                        parentId = attributes.getValue("parentID");
                    }
                    break;
                case "res":
                    element = Element.RES;
                    break;
                case "dc:title":
                    element = Element.TITLE;
                    break;
                case "upnp:class":
                    element = Element.CLASS;
                    break;
                case "dc:creator":
                    element = Element.CREATOR;
                    break;
                case "upnp:album":
                    element = Element.ALBUM;
                    break;
                case "upnp:albumArtURI":
                    element = Element.ALBUM_ART_URI;
                    break;
                case "upnp:originalTrackNumber":
                    element = Element.TRACK_NUMBER;
                    break;
                case "r:resMD":
                    element = Element.RESMD;
                    break;
                default:
                    List<String> curIgnore = ignore;
                    if (curIgnore == null) {
                        curIgnore = new ArrayList<>();
                        curIgnore.add("DIDL-Lite");
                        curIgnore.add("type");
                        curIgnore.add("ordinal");
                        curIgnore.add("description");
                        ignore = curIgnore;
                    }
                    if (!curIgnore.contains(localName)) {
                        LOGGER.debug("Did not recognise element named {}", localName);
                    }
                    element = null;
                    break;
            }
        }
        @Override
        public void characters(char @Nullable [] ch, int start, int length) throws SAXException {
            Element elt = element;
            if (elt == null) {
                return;
            }
            switch (elt) {
                case TITLE:
                    title.append(ch, start, length);
                    break;
                case CLASS:
                    upnpClass.append(ch, start, length);
                    break;
                case RES:
                    res.append(ch, start, length);
                    break;
                case ALBUM:
                    album.append(ch, start, length);
                    break;
                case ALBUM_ART_URI:
                    albumArtUri.append(ch, start, length);
                    break;
                case CREATOR:
                    creator.append(ch, start, length);
                    break;
                case TRACK_NUMBER:
                    trackNumber.append(ch, start, length);
                    break;
                case RESMD:
                    desc.append(ch, start, length);
                    break;
                case DESC:
                    break;
            }
        }
        @Override
        public void endElement(@Nullable String uri, @Nullable String localName, @Nullable String qName)
                throws SAXException {
            if (("container".equals(qName) || "item".equals(qName))) {
                element = null;
                int trackNumberVal = 0;
                try {
                    trackNumberVal = Integer.parseInt(trackNumber.toString());
                } catch (Exception e) {
                }
                SonosResourceMetaData md = null;
                if (!desc.toString().isEmpty()) {
                    try {
                        md = getResourceMetaData(desc.toString());
                    } catch (SAXException ignore) {
                        LOGGER.debug("Failed to parse embeded", ignore);
                    }
                }
                artists.add(new SonosEntry(id, title.toString(), parentId, album.toString(), albumArtUri.toString(),
                        creator.toString(), upnpClass.toString(), res.toString(), trackNumberVal, md));
                title = new StringBuilder();
                upnpClass = new StringBuilder();
                res = new StringBuilder();
                album = new StringBuilder();
                albumArtUri = new StringBuilder();
                creator = new StringBuilder();
                trackNumber = new StringBuilder();
                desc = new StringBuilder();
            }
        }
        public List<SonosEntry> getArtists() {
            return artists;
        }
    }
    private static class ResourceMetaDataHandler extends DefaultHandler {
        private String id = "";
        private String parentId = "";
        private StringBuilder title = new StringBuilder();
        private StringBuilder upnpClass = new StringBuilder();
        private StringBuilder desc = new StringBuilder();
        private @Nullable Element element;
        private @Nullable SonosResourceMetaData metaData;
        ResourceMetaDataHandler() {
        }
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            String name = qName == null ? "" : qName;
            switch (name) {
                case "container":
                case "item":
                    if (attributes != null) {
                        id = attributes.getValue("id");
                        parentId = attributes.getValue("parentID");
                    }
                    break;
                case "desc":
                    element = Element.DESC;
                    break;
                case "upnp:class":
                    element = Element.CLASS;
                    break;
                case "dc:title":
                    element = Element.TITLE;
                    break;
                default:
                    element = null;
                    break;
            }
        }
        @Override
        public void characters(char @Nullable [] ch, int start, int length) throws SAXException {
            Element elt = element;
            if (elt == null) {
                return;
            }
            switch (elt) {
                case TITLE:
                    title.append(ch, start, length);
                    break;
                case CLASS:
                    upnpClass.append(ch, start, length);
                    break;
                case DESC:
                    desc.append(ch, start, length);
                    break;
                default:
                    break;
            }
        }
        @Override
        public void endElement(@Nullable String uri, @Nullable String localName, @Nullable String qName)
                throws SAXException {
            if ("DIDL-Lite".equals(qName)) {
                metaData = new SonosResourceMetaData(id, parentId, title.toString(), upnpClass.toString(),
                        desc.toString());
                element = null;
                desc = new StringBuilder();
                upnpClass = new StringBuilder();
                title = new StringBuilder();
            }
        }
        public @Nullable SonosResourceMetaData getMetaData() {
            return metaData;
        }
    }
    private static class AlarmHandler extends DefaultHandler {
        private @Nullable String id;
        private String startTime = "";
        private String duration = "";
        private String recurrence = "";
        private @Nullable String enabled;
        private String roomUUID = "";
        private String programURI = "";
        private String programMetaData = "";
        private String playMode = "";
        private @Nullable String volume;
        private @Nullable String includeLinkedZones;
        private List<SonosAlarm> alarms = new ArrayList<>();
        AlarmHandler() {
        }
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            if ("Alarm".equals(qName) && attributes != null) {
                id = attributes.getValue("ID");
                duration = attributes.getValue("Duration");
                recurrence = attributes.getValue("Recurrence");
                startTime = attributes.getValue("StartTime");
                enabled = attributes.getValue("Enabled");
                roomUUID = attributes.getValue("RoomUUID");
                programURI = attributes.getValue("ProgramURI");
                programMetaData = attributes.getValue("ProgramMetaData");
                playMode = attributes.getValue("PlayMode");
                volume = attributes.getValue("Volume");
                includeLinkedZones = attributes.getValue("IncludeLinkedZones");
            }
        }
        @Override
        public void endElement(@Nullable String uri, @Nullable String localName, @Nullable String qName)
                throws SAXException {
            if ("Alarm".equals(qName)) {
                int finalID = 0;
                int finalVolume = 0;
                boolean finalEnabled = !"0".equals(enabled);
                boolean finalIncludeLinkedZones = !"0".equals(includeLinkedZones);
                try {
                    finalID = Integer.parseInt(id);
                    finalVolume = Integer.parseInt(volume);
                } catch (Exception e) {
                    LOGGER.debug("Error parsing Integer");
                }
                alarms.add(new SonosAlarm(finalID, startTime, duration, recurrence, finalEnabled, roomUUID, programURI,
                        programMetaData, playMode, finalVolume, finalIncludeLinkedZones));
            }
        }
        public List<SonosAlarm> getAlarms() {
            return alarms;
        }
    }
    private static class ZoneGroupHandler extends DefaultHandler {
        private final List<SonosZoneGroup> groups = new ArrayList<>();
        private final List<String> currentGroupPlayers = new ArrayList<>();
        private final List<String> currentGroupPlayerZones = new ArrayList<>();
        private String coordinator = "";
        private String groupId = "";
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            if ("ZoneGroup".equals(qName) && attributes != null) {
                groupId = attributes.getValue("ID");
                coordinator = attributes.getValue("Coordinator");
            } else if ("ZoneGroupMember".equals(qName) && attributes != null) {
                currentGroupPlayers.add(attributes.getValue("UUID"));
                String zoneName = attributes.getValue("ZoneName");
                if (zoneName != null) {
                    currentGroupPlayerZones.add(zoneName);
                }
                String htInfoSet = attributes.getValue("HTSatChanMapSet");
                if (htInfoSet != null) {
                    currentGroupPlayers.addAll(getAllHomeTheaterMembers(htInfoSet));
                }
            }
        }
        @Override
        public void endElement(@Nullable String uri, @Nullable String localName, @Nullable String qName)
                throws SAXException {
            if ("ZoneGroup".equals(qName)) {
                groups.add(new SonosZoneGroup(groupId, coordinator, currentGroupPlayers, currentGroupPlayerZones));
                currentGroupPlayers.clear();
                currentGroupPlayerZones.clear();
            }
        }
        public List<SonosZoneGroup> getGroups() {
            return groups;
        }
        private Set<String> getAllHomeTheaterMembers(String homeTheaterDescription) {
            Set<String> homeTheaterMembers = new HashSet<>();
            Matcher matcher = Pattern.compile("(RINCON_\\w+)").matcher(homeTheaterDescription);
            while (matcher.find()) {
                String member = matcher.group();
                homeTheaterMembers.add(member);
            }
            return homeTheaterMembers;
        }
    }
    private static class OpmlHandler extends DefaultHandler {
        private final List<String> textFields = new ArrayList<>();
        private @Nullable String textField;
        private @Nullable String type;
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            if ("outline".equals(qName)) {
                type = attributes == null ? null : attributes.getValue("type");
                if ("text".equals(type)) {
                    textField = attributes == null ? null : attributes.getValue("text");
                } else {
                    textField = null;
                }
            }
        }
        @Override
        public void endElement(@Nullable String uri, @Nullable String localName, @Nullable String qName)
                throws SAXException {
            if ("outline".equals(qName)) {
                String field = textField;
                if (field != null) {
                    textFields.add(field);
                }
            }
        }
        public List<String> getTextFields() {
            return textFields;
        }
    }
    private static class AVTransportEventHandler extends DefaultHandler {
        private final Map<String, @Nullable String> changes = new HashMap<>();
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            if (localName == null) {
                LOGGER.info("{} is not defined in EventType. ", localName);
            } else {
                String val = attributes == null ? null : attributes.getValue("val");
                if (val != null) {
                    changes.put(localName, val);
                }
            }
        }
        public Map<String, @Nullable String> getChanges() {
            return changes;
        }
    }
    private static class MetaDataHandler extends DefaultHandler {
        private @Nullable CurrentElement currentElement;
        private String id = "-1";
        private String parentId = "-1";
        private StringBuilder resource = new StringBuilder();
        private StringBuilder streamContent = new StringBuilder();
        private StringBuilder albumArtUri = new StringBuilder();
        private StringBuilder title = new StringBuilder();
        private StringBuilder upnpClass = new StringBuilder();
        private StringBuilder creator = new StringBuilder();
        private StringBuilder album = new StringBuilder();
        private StringBuilder albumArtist = new StringBuilder();
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            String name = localName == null ? "" : localName;
            switch (name) {
                case "item":
                    currentElement = CurrentElement.item;
                    if (attributes != null) {
                        id = attributes.getValue("id");
                        parentId = attributes.getValue("parentID");
                    }
                    break;
                case "res":
                    currentElement = CurrentElement.res;
                    break;
                case "streamContent":
                    currentElement = CurrentElement.streamContent;
                    break;
                case "albumArtURI":
                    currentElement = CurrentElement.albumArtURI;
                    break;
                case "title":
                    currentElement = CurrentElement.title;
                    break;
                case "class":
                    currentElement = CurrentElement.upnpClass;
                    break;
                case "creator":
                    currentElement = CurrentElement.creator;
                    break;
                case "album":
                    currentElement = CurrentElement.album;
                    break;
                case "albumArtist":
                    currentElement = CurrentElement.albumArtist;
                    break;
                default:
                    currentElement = null;
                    break;
            }
        }
        @Override
        public void characters(char @Nullable [] ch, int start, int length) throws SAXException {
            CurrentElement elt = currentElement;
            if (elt == null) {
                return;
            }
            switch (elt) {
                case item:
                    break;
                case res:
                    resource.append(ch, start, length);
                    break;
                case streamContent:
                    streamContent.append(ch, start, length);
                    break;
                case albumArtURI:
                    albumArtUri.append(ch, start, length);
                    break;
                case title:
                    title.append(ch, start, length);
                    break;
                case upnpClass:
                    upnpClass.append(ch, start, length);
                    break;
                case creator:
                    creator.append(ch, start, length);
                    break;
                case album:
                    album.append(ch, start, length);
                    break;
                case albumArtist:
                    albumArtist.append(ch, start, length);
                    break;
                case desc:
                    break;
            }
        }
        public SonosMetaData getMetaData() {
            return new SonosMetaData(id, parentId, resource.toString(), streamContent.toString(),
                    albumArtUri.toString(), title.toString(), upnpClass.toString(), creator.toString(),
                    album.toString(), albumArtist.toString());
        }
    }
    private static class RenderingControlEventHandler extends DefaultHandler {
        private final Map<String, @Nullable String> changes = new HashMap<>();
        private boolean getPresetName = false;
        private @Nullable String presetName;
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            if (qName == null) {
                return;
            }
            String channel;
            String val;
            switch (qName) {
                case "Volume":
                case "Mute":
                case "Loudness":
                    channel = attributes == null ? null : attributes.getValue("channel");
                    val = attributes == null ? null : attributes.getValue("val");
                    if (channel != null && val != null) {
                        changes.put(qName + channel, val);
                    }
                    break;
                case "Bass":
                case "Treble":
                case "OutputFixed":
                    val = attributes == null ? null : attributes.getValue("val");
                    if (val != null) {
                        changes.put(qName, val);
                    }
                    break;
                case "PresetNameList":
                    getPresetName = true;
                    break;
                default:
                    break;
            }
        }
        @Override
        public void characters(char @Nullable [] ch, int start, int length) throws SAXException {
            if (getPresetName) {
                presetName = new String(ch, start, length);
            }
        }
        @Override
        public void endElement(@Nullable String uri, @Nullable String localName, @Nullable String qName)
                throws SAXException {
            if (getPresetName) {
                getPresetName = false;
                String preset = presetName;
                if (qName != null && preset != null) {
                    changes.put(qName, preset);
                }
            }
        }
        public Map<String, @Nullable String> getChanges() {
            return changes;
        }
    }
    private static class MusicServiceHandler extends DefaultHandler {
        private final List<SonosMusicService> services = new ArrayList<>();
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            if ("Service".equals(qName) && attributes != null && attributes.getValue("Id") != null
                    && attributes.getValue("Name") != null) {
                services.add(new SonosMusicService(attributes.getValue("Id"), attributes.getValue("Name")));
            }
        }
        public List<SonosMusicService> getServices() {
            return services;
        }
    }
    public static @Nullable String getRoomName(String descriptorXML) {
        RoomNameHandler roomNameHandler = new RoomNameHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(roomNameHandler);
            URL url = new URL(descriptorXML);
            reader.parse(new InputSource(url.openStream()));
        } catch (IOException | SAXException e) {
            LOGGER.error("Could not parse Sonos room name from string '{}'", descriptorXML);
        }
        return roomNameHandler.getRoomName();
    }
    private static class RoomNameHandler extends DefaultHandler {
        private @Nullable String roomName;
        private boolean roomNameTag;
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            if ("roomName".equalsIgnoreCase(localName)) {
                roomNameTag = true;
            }
        }
        @Override
        public void characters(char @Nullable [] ch, int start, int length) throws SAXException {
            if (roomNameTag) {
                roomName = new String(ch, start, length);
                roomNameTag = false;
            }
        }
        public @Nullable String getRoomName() {
            return roomName;
        }
    }
    public static @Nullable String parseModelDescription(URL descriptorURL) {
        ModelNameHandler modelNameHandler = new ModelNameHandler();
        try {
            XMLReader reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(modelNameHandler);
            URL url = new URL(descriptorURL.toString());
            reader.parse(new InputSource(url.openStream()));
        } catch (IOException | SAXException e) {
            LOGGER.error("Could not parse Sonos model name from string '{}'", descriptorURL.toString());
        }
        return modelNameHandler.getModelName();
    }
    private static class ModelNameHandler extends DefaultHandler {
        private @Nullable String modelName;
        private boolean modelNameTag;
        @Override
        public void startElement(@Nullable String uri, @Nullable String localName, @Nullable String qName,
                @Nullable Attributes attributes) throws SAXException {
            if ("modelName".equalsIgnoreCase(localName)) {
                modelNameTag = true;
            }
        }
        @Override
        public void characters(char @Nullable [] ch, int start, int length) throws SAXException {
            if (modelNameTag) {
                modelName = new String(ch, start, length);
                modelNameTag = false;
            }
        }
        public @Nullable String getModelName() {
            return modelName;
        }
    }
    public static String extractModelName(String sonosModelName) {
        String ret = sonosModelName;
        Matcher matcher = Pattern.compile("\\s(.*)").matcher(ret);
        if (matcher.find()) {
            ret = matcher.group(1);
        }
        if (ret.contains(":")) {
            ret = ret.replace(":", "");
        }
        return ret;
    }
    public static String compileMetadataString(SonosEntry entry) {
        String id = entry.getId();
        String parentId = entry.getParentId();
        String title = entry.getTitle();
        String upnpClass = entry.getUpnpClass();
        String desc = entry.getDesc();
        if (desc == null) {
            desc = "RINCON_AssociatedZPUDN";
        }
        SonosResourceMetaData resourceMetaData = entry.getResourceMetaData();
        if (resourceMetaData != null) {
            id = resourceMetaData.getId();
            parentId = resourceMetaData.getParentId();
            title = resourceMetaData.getTitle();
            desc = resourceMetaData.getDesc();
            upnpClass = resourceMetaData.getUpnpClass();
        }
        title = StringEscapeUtils.escapeXml(title);
        String metadata = METADATA_FORMAT.format(new Object[] { id, parentId, title, upnpClass, desc });
        return metadata;
    }
}
