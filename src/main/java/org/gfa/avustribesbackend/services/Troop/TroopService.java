package org.gfa.avustribesbackend.services.Troop;

import org.gfa.avustribesbackend.models.Kingdom;

public interface TroopService {
  void create(Kingdom kingdom);

  void eat();

  void upgrade(Kingdom kingdom);
}
