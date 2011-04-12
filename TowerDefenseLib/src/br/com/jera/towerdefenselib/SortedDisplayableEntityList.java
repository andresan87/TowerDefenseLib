package br.com.jera.towerdefenselib;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

import br.com.jera.util.Classic2DViewer;
import br.com.jera.util.CommonMath.Rectangle2D;
import br.com.jera.util.DisplayableEntity;
import br.com.jera.util.SpriteResourceManager;

public class SortedDisplayableEntityList extends LinkedList<DisplayableEntity> {
	private static final long serialVersionUID = 1L;

	// Utlizado para evitar crash devido a um problema não identificado
	private void sortMaroto() {
		try {
			Collections.sort(this);
		} catch (Exception e) {
			// dummy
		}
	}

	public void draw(Classic2DViewer viewer, SpriteResourceManager res) {
		sortMaroto();

		ListIterator<DisplayableEntity> iter = super.listIterator();
		while (iter.hasNext()) {
			try {
				DisplayableEntity ent = (DisplayableEntity) iter.next();
				ent.draw(viewer, res);
			} catch (Exception e) {
				continue;
			}
		}
		super.clear();
	}

	public boolean sortAdd(Classic2DViewer viewer, DisplayableEntity o, Rectangle2D clientRect) {
		if (!o.isVisible(viewer, clientRect)) {
			return false;
		}
		super.add(o);
		return true;
	}
}
