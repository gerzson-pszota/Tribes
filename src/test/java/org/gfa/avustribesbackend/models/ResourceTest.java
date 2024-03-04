package org.gfa.avustribesbackend.models;

import org.gfa.avustribesbackend.models.enums.ResourceTypeValue;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceTest {

  @Test
  public void test_default_constructor() {

    Resource resource = new Resource();

    Long manuallySetId = 123L;
    resource.setId(manuallySetId);

    assertNotNull(resource);
    assertEquals(manuallySetId, resource.getId());
  }

  @Test
  public void test_constructor_with_parameters() {

    Kingdom kingdom = new Kingdom();

    int amount = 0;
    Long manuallySetId = 666L;

    Resource resource = new Resource(kingdom, ResourceTypeValue.FOOD);
    resource.setId(manuallySetId);

    assertNotNull(resource);
    assertEquals(manuallySetId, resource.getId());
    assertEquals(kingdom, resource.getKingdom());
    assertEquals(amount, resource.getAmount());
  }
}
