package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BoardColorSchemeTest {

    @Test
    public void testConstructors(){
        assertEquals(new BoardColorScheme().getBoardBackgroundColor(), "0xffffff");
    }

    @Test
    public void testGetters(){
        BoardColorScheme s = new BoardColorScheme();
        assertEquals(s.getBoardBackgroundColor(), "0xffffff");
        assertEquals(s.getBoardTextColor(), "0x000000");
        assertEquals(s.getListBackgroundColor(), "0xffffff");
        assertEquals(s.getListTextColor(), "0x000000");
    }

    @Test
    public void testSetters(){
        BoardColorScheme s = new BoardColorScheme();
        s.setBoardBackgroundColor("0x111111");
        s.setBoardTextColor("0xAAAAAA");
        s.setListBackgroundColor("0xEEEEEE");
        s.setListTextColor("0x444444");
        assertEquals(s.getBoardBackgroundColor(), "0x111111");
        assertEquals(s.getBoardTextColor(), "0xAAAAAA");
        assertEquals(s.getListBackgroundColor(), "0xEEEEEE");
        assertEquals(s.getListTextColor(), "0x444444");
    }

    @Test
    public void testToString(){
        BoardColorScheme s = new BoardColorScheme();
        assertEquals("BoardColorScheme{id=0, boardBackgroundColor='0xffffff', boardTextColor='0x000000', listBackgroundColor='0xffffff', listTextColor='0x000000', taskPresets=}", s.toString());
    }
}
