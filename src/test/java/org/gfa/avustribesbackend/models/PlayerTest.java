package org.gfa.avustribesbackend.models;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

  @Test
  public void player_is_created_after_calling_constructor() {
    String userName = "Michal";
    String email = "abcd@email.com";
    String password = "awd3aw63d5aw3";
    Date verifiedAt = new Date(System.currentTimeMillis());

    Player player = new Player(userName, email, password);

    assertNotNull(player);
    assertEquals(userName, player.getPlayerName());
    assertEquals(email, player.getEmail());
    assertEquals(password, player.getPassword());
  }
}
