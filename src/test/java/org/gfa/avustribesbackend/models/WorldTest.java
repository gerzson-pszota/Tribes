package org.gfa.avustribesbackend.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorldTest {

  @Test
  void test_generator_of_name() {
    World world = new World();
    String name = world.generatorOfName();
    assertNotNull(name);
  }

  @Test
  void test_of_default_constructor() {
    World world = new World();
    assertNotNull(world, world.getName());
  }

  @Test
  void test_of_constructor_with_param() {
    World world = new World("Erebor");
    assertEquals("Erebor", world.getName());
  }
}
