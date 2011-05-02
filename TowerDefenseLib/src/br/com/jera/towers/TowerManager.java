package br.com.jera.towers;

import java.util.LinkedList;
import java.util.ListIterator;

import br.com.jera.audio.AudioPlayer;
import br.com.jera.enemies.Enemy;
import br.com.jera.graphic.Sprite;
import br.com.jera.input.InputListener;
import br.com.jera.resources.PropertyReader;
import br.com.jera.resources.ResourceIdRetriever;
import br.com.jera.towerdefenselib.OutputData;
import br.com.jera.towerdefenselib.SortedDisplayableEntityList;
import br.com.jera.util.BitmapFont;
import br.com.jera.util.Classic2DViewer;
import br.com.jera.util.CommonMath.Rectangle2D;
import br.com.jera.util.CommonMath.Vector2;
import br.com.jera.util.SpriteResourceManager;
import br.com.jera.weapons.Net;
import br.com.jera.weapons.ProjectileManager;
import br.com.jera.weapons.WeaponProfile;

public class TowerManager implements OutputData.Data {

	public TowerManager(ResourceIdRetriever resRet) {
		this.resRet = resRet;
		controller = new BehaviourController(resRet);
	}

	public void update(final long lastFrameDeltaTimeMS, LinkedList<Enemy> zombies, ProjectileManager projectileManager,
			AudioPlayer audioPlayer) {
		ListIterator<Tower> iter = vikings.listIterator();
		while (iter.hasNext()) {
			Tower viking = iter.next();
			this.shootPriority = controller.getCurrentState();
			ListIterator<Enemy> ziter = zombies.listIterator();
			Enemy zombieToShoot = null;
			while (ziter.hasNext()) {
				Enemy zombie = ziter.next();
				WeaponProfile weapon = viking.getWeapon();
				if (viking.get2DPos().distance(zombie.get2DPos()) < weapon.getRange()
						&& zombie.getInitialHp() + zombie.getDamageReceived() > 0) {
					if (viking.getWeapon() instanceof Net && zombie.isNetLaunched()) {
						continue;
					}
					if (zombieToShoot == null) {
						zombieToShoot = zombie;
					} else {
						if (ShootPriority.WEAKEST.equals(this.shootPriority)) {
							if (zombie.getNormalizedHp() < zombieToShoot.getNormalizedHp()) {
								zombieToShoot = zombie;
							}
						} else if (ShootPriority.FURTHEST.equals(this.shootPriority)) {
							if (zombie.getNextTile() > zombieToShoot.getNextTile()) {
								zombieToShoot = zombie;
							}
						} else if (ShootPriority.FIFO.equals(this.shootPriority)) {
							zombieToShoot = zombie;
							break;
						}
					}
				}
			}

			if (zombieToShoot != null && viking.trigger(projectileManager, zombieToShoot, audioPlayer)) {
				viking.setDirection(zombieToShoot.get2DPos().sub(viking.get2DPos()).normalize());
			}
			viking.update(lastFrameDeltaTimeMS, audioPlayer);
		}
	}

	public void draw(Classic2DViewer viewer, SortedDisplayableEntityList displayList, Rectangle2D clientRect, SpriteResourceManager res,
			AudioPlayer audioPlayer, InputListener input) {
		ListIterator<Tower> iter = vikings.listIterator();
		while (iter.hasNext()) {
			displayList.sortAdd(viewer, iter.next(), clientRect);
		}
		if (!PropertyReader.isHideBehaviourController()) {
			controller.putButtons(res, audioPlayer, input);
		}
	}

	public boolean addViking(TowerProfile profile, Vector2 pos, AudioPlayer audioPlayer) {
		if (vikings.size() < MAXIMUM_VIKINGS) {
			vikings.add(new Tower(profile, pos, audioPlayer, resRet));
			return true;
		} else {
			return false;
		}
	}

	public boolean hasUnityIn(Vector2 p) {
		ListIterator<Tower> iter = vikings.listIterator();
		while (iter.hasNext()) {
			if (p.distance(iter.next().get2DPos()) < Tower.defaultRadius)
				return true;
		}
		return false;
	}

	public Vector2 drawData(Vector2 pos, SpriteResourceManager res, BitmapFont font) {
		Sprite sprite = res.getSprite(resRet.getBmpTowerSymbol());
		sprite.draw(pos, Sprite.defaultOrigin);
		final Vector2 frameSize = sprite.getFrameSize();
		String str = new Integer(vikings.size()).toString();
		str += "/";
		str += new Integer(MAXIMUM_VIKINGS).toString();
		if (vikings.size() >= MAXIMUM_VIKINGS) {
			str += (char) 0x07;
		}
		font.draw(new Vector2(frameSize.x, 0).add(pos), str);
		return new Vector2(0, frameSize.y);
	}

	private LinkedList<Tower> vikings = new LinkedList<Tower>();
	private static final int MAXIMUM_VIKINGS = 45;
	private ShootPriority shootPriority;
	private ResourceIdRetriever resRet;
	private BehaviourController controller;

	protected enum ShootPriority {
		FIFO, WEAKEST, FURTHEST
	}
}
