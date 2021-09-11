package fr.bafbi.qlfsky.utils;

import org.bukkit.entity.Player;

public class TpaInvitation {

    private Player invitator;
    private Player invitate;
    private Integer schedulId;
    private Integer timeLeft;
    
    public TpaInvitation(Player invitator, Player invitate, Integer schedulId, Integer timeLeft) {
        this.invitator = invitator;
        this.invitate = invitate;
        this.schedulId = schedulId;
        this.timeLeft = timeLeft;
    }
    public Player getInvitator() {
        return invitator;
    }
    public Integer getTimeLeft() {
        return timeLeft;
    }
    public void setTimeLeft(Integer timeLeft) {
        this.timeLeft = timeLeft;
    }
    public Integer getSchedulId() {
        return schedulId;
    }
    public void setSchedulId(Integer schedulId) {
        this.schedulId = schedulId;
    }
    public Player getInvitate() {
        return invitate;
    }
    public void setInvitate(Player invitate) {
        this.invitate = invitate;
    }
    public void setInvitator(Player invitator) {
        this.invitator = invitator;
    }
    
}
