package org.gfa.avustribesbackend.services.Kingdom;

import jakarta.servlet.http.HttpServletRequest;
import org.gfa.avustribesbackend.dtos.KingdomResponseDTO;
import org.gfa.avustribesbackend.dtos.PlayerKingdomResponseDTO;
import org.gfa.avustribesbackend.models.Kingdom;
import org.gfa.avustribesbackend.models.Player;

import java.util.List;

public interface KingdomService {

  KingdomResponseDTO createKingdomDTO(Kingdom kingdom);

  List<KingdomResponseDTO> listKingdoms();

  List<PlayerKingdomResponseDTO> listPlayerKingdoms(HttpServletRequest request);

  PlayerKingdomResponseDTO getPlayerKingdomResponseDTO(Kingdom kingdom);

  KingdomResponseDTO returnKingdomDTOById(Long id);

  boolean checkId(Long id);

  void createStartingKingdom(Player player);

  double[] generateCoordinates();
}
