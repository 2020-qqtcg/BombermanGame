package thing;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class ThingTest {
    Random r = new Random();

    @Test
    public void positionTest(){
        Thing thing = new Thing((char)0, 1);
        assertEquals((char)0, thing.getSign());

        for(int i = 0; i < 10; i++){
            int x = r.nextInt(25);
            int y = r.nextInt(25);
            thing.setPosition(x, y);
            assertEquals(x ,thing.getX());
            assertEquals(y, thing.getY());
        }

    }

    @Test
    public void liveTest(){
        Thing thing = new Thing((char)0, 1);
        thing.care();
        assertTrue(thing.isLive());
        thing.hurt();
        assertEquals(1, thing.getLive());

        assertTrue(thing.beBroke());
    }



}