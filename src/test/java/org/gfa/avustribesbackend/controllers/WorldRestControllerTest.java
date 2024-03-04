package org.gfa.avustribesbackend.controllers;

import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.Player;
import org.gfa.avustribesbackend.models.World;
import org.gfa.avustribesbackend.repositories.KingdomRepository;
import org.gfa.avustribesbackend.repositories.PlayerRepository;
import org.gfa.avustribesbackend.repositories.WorldRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class WorldRestControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private WorldRepository worldRepository;
  @Autowired private KingdomRepository kingdomRepository;
  @Autowired private PlayerRepository playerRepository;

  @Test
  void create_and_return_world() throws Exception {
    // Arrange
    World world = new World();
    world.setId(1L);

    Player player = new Player("Quest", "hello@gmail.com",
            "ILovePassword");

    Kingdom kingdom = new Kingdom("Utopia", 40.5, 45.4, player, world);
    kingdom.setId(1L);

    worldRepository.save(world);
    playerRepository.save(player);
    kingdomRepository.save(kingdom);

    // Assert
    mockMvc
        .perform(get("/worlds"))
        .andExpect(status().is(200))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }

  @Test
  void no_worlds() throws Exception {
    mockMvc
        .perform(get("/worlds"))
        .andExpect(status().is(404))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andDo(print());
  }
}
