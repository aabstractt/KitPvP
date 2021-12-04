package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.provider.PlayerStorage;
import dev.thatsmybaby.zone.ZoneFactory;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent ev) {
        if(ev.getEntity() instanceof Player) {
            Player player = (Player) ev.getEntity();

            if (ZoneFactory.getInstance().inZone(player)) {
                ev.setCancelled();

                return;
            }

            if(ev.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && ev.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
                ev.setCancelled(true);

                return;
            }

            return;
        }

        ev.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent ev) {
        if(ev.getDamager() instanceof Player && ev.getEntity() instanceof Player) {

            Player damager = (Player) ev.getDamager();
            Player entity = (Player) ev.getEntity();

            if (ZoneFactory.getInstance().inZone(damager)) {
                ev.setCancelled();

                return;
            }

            if (ZoneFactory.getInstance().inZone(entity)) {
                ev.setCancelled();

                return;
            }

            PlayerStorage playerStorage = PlayerStorage.of(entity);
            PlayerStorage targetStorage = PlayerStorage.of(damager);

            if (playerStorage == null || targetStorage == null) {
                ev.setCancelled();

                return;
            }

            if (cancelAttack(playerStorage, damager) || cancelAttack(targetStorage, entity)) {
                ev.setCancelled();

                return;
            }

            playerStorage.attack(damager);
            targetStorage.attack(entity);

            double dmg = entity.getHealth() - ev.getFinalDamage();

            if(dmg < 1.0) {
                ev.setCancelled(true);

                handleDeath(entity, damager);

                targetStorage.increaseKills();
                playerStorage.death();

                targetStorage.attack(null);
                playerStorage.attack(null);

                Server.getInstance().getScheduler().scheduleDelayedTask(KitPvP.getInstance(), () -> entity.setHealth(20), 3);
            }

            return;
        }

        ev.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByChildEntityEvent(EntityDamageByChildEntityEvent ev) {
        if(ev.getChild() instanceof EntityArrow && ev.getEntity() instanceof Player) {
            Player shooter = (Player) ev.getDamager();
            Player entity = (Player) ev.getEntity();

            if (ZoneFactory.getInstance().inZone(shooter)) {
                ev.setCancelled();

                return;
            }

            if (ZoneFactory.getInstance().inZone(entity)) {
                ev.setCancelled();

                return;
            }

            if(shooter.getUniqueId().equals(entity.getUniqueId())) {
                ev.setCancelled(true);

                return;
            }

            PlayerStorage playerStorage = PlayerStorage.of(entity);
            PlayerStorage targetStorage = PlayerStorage.of(shooter);

            if (playerStorage == null || targetStorage == null) {
                ev.setCancelled();

                return;
            }

            if (cancelAttack(playerStorage, shooter) || cancelAttack(targetStorage, entity)) {
                ev.setCancelled();

                return;
            }

            playerStorage.attack(shooter);
            targetStorage.attack(entity);

            float dmg = entity.getHealth() - ev.getFinalDamage();

            if(dmg < 1.0) {
                ev.setCancelled(true);

                handleDeath(entity, shooter);

                targetStorage.increaseKills();
                playerStorage.death();

                targetStorage.attack(null);
                playerStorage.attack(null);

                Server.getInstance().getScheduler().scheduleDelayedTask(KitPvP.getInstance(), () -> entity.setHealth(20), 3);
            }

            return;
        }

        ev.setCancelled(true);
    }

    private boolean cancelAttack(PlayerStorage playerStorage, Player target) {
        Player lastAttack = playerStorage.getLastAttack();

        return lastAttack != null && lastAttack.equals(target);
    }

    private void handleDeath(Player player, Player killer) {
        String message = KitPvP.getInstance().replacePlaceholders("PLAYER_KILLED", "<player>", player.getName());

        if (killer != null) {
            message = KitPvP.getInstance().replacePlaceholders("PLAYER_KILLED_BY", "<player>", player.getName(), "<killer>", killer.getName());
        }

        for (Player target : player.getLevel().getPlayers().values()) {
            target.sendMessage(message);
        }
    }
}