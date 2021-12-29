package thing;


import org.junit.Test;

import static org.junit.Assert.*;


public class WorldTest {
    World world = new World();

    @Test
    public void hpTest(){
        assertEquals(world.getHP(), 2);

    }

    @Test
    public void Test2(){
        assertFalse(world.setMonster(new Monster(1, world), 0, 0));
        world.getPlayer1().attack();
        assertNotEquals(world.getThings()[0][0], world.getPlayer1());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertFalse(world.setMonster(new Monster(1, world), 0, 0));
    }

}