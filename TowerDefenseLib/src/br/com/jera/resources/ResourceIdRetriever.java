package br.com.jera.resources;

public interface ResourceIdRetriever {
	public int getBmpWhite();
	public int getBmpProgessBar();
	public int getBmpTower(int id);
	public int getBmpEnemy(int id);
	public int getBmpTiles();
	public int getBmpDeathAnim();
	public int getBmpNextWaveSymbol();
	public int getBmpScoreSymbol();
	public int getBmpScenario(int id);
	public int getBmpThemeFont16();
	public int getBmpShadow();
	public int getBmpRange();
	public int getBmpWeaponProjectile(int id);
	public int getBmpWeaponHitEffect(int id);
	public int getBmpScenarioSeam();
	public int getBmpMoneySymbol();
	public int getBmpTowerSymbol();
	public int getBmpMenu(int id);
	public int getBmpGameOver();
	public int getBmpGameWon();
	public int getBmpForwarButton();
	public int getBmpDefaultFont16();
	public int getBmpMenuButtons();
	public int getBmpMenuBackground();
	public int getBmpMenuLogo();
	public int getBmpSoundToggle();
	public int getBmpCompanyLogo();
	public int getBmpSplashScreenBg();
	public int getBmpSideBar();
	public int getBmpSideBarExtend();
	public int getBmpSideBarBottom();
	public int getBmpNextFrameButton();
	public int getBmpSkipTutorialButton();
	public int getBmpClockHelpCharacter();
	public int getBmpClockHelpBalloon();
	public int getBmpClockHelpTexts();
	public int getBmpSelectmapBg();

	public int getBmpHelpFrame(int frame);
	public int getNumHelpFrames();
	
	public int getBmpTwitterButton();
	public int getBmpFacebookButton();

	public int getSfxStart();
	public int getSfxEnemyDeath();
	public int getSfxEnemyDeath(int id);
	public int getSfxGameOver();
	public int getSfxGameWon();
	public int getSfxWeaponTrigger(int id);
	public int getSfxWeaponHit(int id);
	public int getSfxBack();
	public int getSfxMenuButtonPressed();
	public int getSfxTowerDrag();
	public int getSfxTowerDrop();
	public int getSfxNextLevel();

	public int getSfxMenuSong();
	public int getBmpClockSymbol();
	public int getBmpSpeedButtons();
	public int getBmpBackButton();
	public int getBmpNextWaveButton();
}
