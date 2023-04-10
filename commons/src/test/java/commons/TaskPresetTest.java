package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TaskPresetTest {

    @Test
    public void testConstructors(){
        assertFalse(new TaskPreset("WOW").isDefault());
        assertFalse(new TaskPreset().isDefault());
        assertFalse(new TaskPreset("The name", "0x123456", "0x654321").isDefault());
    }

    @Test
    public void testGetters(){
        TaskPreset preset = new TaskPreset("Name", "color", "other color");
        assertEquals("Name", preset.getName());
        assertEquals("color", preset.getBackgroundColor());
        assertEquals("other color", preset.getFontColor());
        assertFalse(preset.isDefault());
    }

    @Test
    public void testSetters(){
        TaskPreset preset = new TaskPreset("Name", "color", "other color");
        preset.setName("qq");
        preset.setBackgroundColor("other color");
        preset.setFontColor("color");
        preset.setDefault(true);
        assertNotEquals("Name", preset.getName());
        assertNotEquals("color", preset.getBackgroundColor());
        assertNotEquals("other color", preset.getFontColor());
        assertTrue(preset.isDefault());
    }

    @Test
    public void testEquals(){
        TaskPreset preset = new TaskPreset("Name", "color", "other color");
        TaskPreset preset2 = new TaskPreset("Name", "color", "other color");
        TaskPreset preset3 = new TaskPreset("Name", "color", "other color");
        preset3.setDefault(true);
        assertEquals(preset, preset2);
        assertNotEquals(preset2, preset3);
    }

    @Test
    public void testHashCode(){
        TaskPreset preset = new TaskPreset("Name", "color", "other color");
        TaskPreset preset2 = new TaskPreset("Name", "color", "other color");
        TaskPreset preset3 = new TaskPreset("Name", "color", "other color");
        preset3.setDefault(true);
        assertEquals(preset.hashCode(), preset2.hashCode());
        assertNotEquals(preset2.hashCode(), preset3.hashCode());
    }

    @Test
    public void testToString(){
        TaskPreset preset = new TaskPreset("Name", "color", "other color");
        assertEquals("Name", preset.toString());
    }
}
