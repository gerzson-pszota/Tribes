package org.gfa.avustribesbackend.controllers;

import org.gfa.avustribesbackend.services.World.WorldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/worlds")
public class WorldRestController {
  private final WorldService worldService;

  @Autowired
  public WorldRestController(WorldService worldService) {
    this.worldService = worldService;
  }

  @GetMapping()
  public ResponseEntity<Object> index() {
    return worldService.index();
  }
}
