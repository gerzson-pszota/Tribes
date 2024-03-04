package org.gfa.avustribesbackend.services.World;

import org.gfa.avustribesbackend.dtos.WorldResponseDto;
import org.gfa.avustribesbackend.exceptions.NotFoundException;
import org.gfa.avustribesbackend.models.World;
import org.gfa.avustribesbackend.repositories.KingdomRepository;
import org.gfa.avustribesbackend.repositories.WorldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorldServiceImpl implements WorldService {
  private final WorldRepository worldRepository;
  private final KingdomRepository kingdomRepository;

  @Autowired
  public WorldServiceImpl(WorldRepository worldRepository, KingdomRepository kingdomRepository) {
    this.worldRepository = worldRepository;
    this.kingdomRepository = kingdomRepository;
  }

  @Override
  public ResponseEntity<Object> index() {
    List<World> allWorlds = worldRepository.findAll();
    List<WorldResponseDto> allDTOs = new ArrayList<>();
    for (World world : allWorlds) {
      WorldResponseDto worldResponseDto =
          new WorldResponseDto(
              world.getId(), world.getName(), kingdomRepository.countAllByWorld_Id(world.getId()));
      allDTOs.add(worldResponseDto);
    }
    if (allDTOs.isEmpty()) {
      throw new NotFoundException("No World!");
    }
    return ResponseEntity.status(200).body(allDTOs);
  }
}
