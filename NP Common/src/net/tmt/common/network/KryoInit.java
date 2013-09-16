package net.tmt.common.network;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.tmt.common.network.dtos.AsteroidDTO;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PacketDTO;
import net.tmt.common.network.dtos.PlayerDTO;
import net.tmt.common.network.dtos.ServerInfoDTO;
import net.tmt.common.util.Vector2d;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

public class KryoInit {

	public static void init(final EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();

		kryo.register(AsteroidDTO.class);
		kryo.register(DTO.class);
		kryo.register(EntityDTO.class);
		kryo.register(PacketDTO.class);
		kryo.register(PlayerDTO.class);
		kryo.register(ServerInfoDTO.class);

		kryo.register(Vector2d.class);
		kryo.register(List.class);
		kryo.register(Color.class);
		kryo.register(float[].class);
		kryo.register(ArrayList.class);
	}
}
