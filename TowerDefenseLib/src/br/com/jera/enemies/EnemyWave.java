package br.com.jera.enemies;

import java.util.ArrayList;

public class EnemyWave {

	public EnemyWave(ArrayList<Enemy> zombies) {
		this.enemy = zombies;
	}

	public ArrayList<Enemy> getEnemies() {
		return enemy;
	}

	private ArrayList<Enemy> enemy;
}
