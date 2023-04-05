package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    @Test
    public void checkConstructor() {
        Tag testTag = new Tag("Tag1", "FFFFFF");
        assertNotNull(testTag);
        assertEquals(testTag.getName(), "Tag1");
    }

    @Test
    public void checkEmptyConstructor() {
        Tag testTag = new Tag();
        assertNotNull(testTag);
        assertEquals(testTag.getName(), "");
    }

    @Test
    public void testGetters() {
        Tag testTag = new Tag("Tag1", "111111");
        assertEquals(testTag.getName(), "Tag1");
        assertEquals(testTag.getColorBackground(), "111111");
    }

    @Test
    public void testSetters() {
        Tag testTag = new Tag("Tag1", "AA4A4A");
        testTag.setName("Tag2");
        assertEquals(testTag.getName(), "Tag2");
        testTag.setColors("FFFFFF", "FFFFFF");
        assertNotEquals(testTag.getColorBackground(), "AA4A4A");
        assertNotEquals(testTag.getColorFont(), "AA4A4A");
    }

    @Test
    public void testEquals() {
        Tag testTag = new Tag("Tag1", "AA4A4A");
        Tag testTag2 = new Tag("Tag1", "AA4A4A");
        Tag testTag3 = new Tag("Tag3", "AA4A4A");
        assertEquals(testTag2, testTag);
        assertNotEquals(testTag3, testTag);
    }

    @Test
    public void testHashCode() {
        Tag testTag = new Tag("Tag1", "555555");
        Tag testTag2 = new Tag("Tag1", "555555");
        Tag testTag3 = new Tag("Tag1", "555655");
        assertEquals(testTag.hashCode(), testTag2.hashCode());
        assertNotEquals(testTag.hashCode(), testTag3.hashCode());
    }

    @Test
    public void testToString() {
        Tag testTag = new Tag("Tag1", "FFFFFF");
        assertNotNull(testTag.toString());
    }
}
