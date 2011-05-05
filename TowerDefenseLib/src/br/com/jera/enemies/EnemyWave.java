package br.com.jera.enemies;

import java.util.ArrayList;

public class EnemyWave {

	public EnemyWave(ArrayList<Enemy> zombies, Integer soundFx) {
		this.enemy = zombies;
		this.soundFx = soundFx;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemy;
	}

	public Integer getSoundFx() {
		return soundFx;
	}

	private ArrayList<Enemy> enemy;
	private Integer soundFx;
}
