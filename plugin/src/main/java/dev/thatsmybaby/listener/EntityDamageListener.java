package dev.thatsmybaby.listener;

import cn.nukkit.Player;
import cn.nukkit.entity.projectile.EntityArrow;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageByChildEntityEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.TaskUtils;
import dev.thatsmybaby.provider.MysqlProvider;
import dev.thatsmybaby.provider.PlayerStorage;
import dev.thatsmybaby.rank.RankFactory;
import dev.thatsmybaby.zone.ZoneFactory;

public class EntityDamageListener implements Listener {

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent ev) {
        if (!(ev.getEntity() instanceof Player)) {
            ev.setCancelled();

            return;
        }

        if (ZoneFactory.getInstance().inZone(ev.getEntity())) {
            ev.setCancelled();

            return;
        }

        if (ev.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && ev.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            ev.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent ev) {
        if (!(ev.getDamager() instanceof Player) || !(ev.getEntity() instanceof Player)) {
            ev.setCancelled(true);

            return;
        }

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

        if (playerStorage.attack(damager)) {
            entity.sendMessage(KitPvP.replacePlaceholders("NEW_ATTACK", "<player>", damager.getName()));
        }

        if (targetStorage.attack(entity)) {
            damager.sendMessage(KitPvP.replacePlaceholders("NEW_ATTACK", "<player>", entity.getName()));
        }

        double dmg = entity.getHealth() - ev.getFinalDamage();

        if (dmg < 1.0) {
            ev.setCancelled(true);

            handleDeath(entity, damager, playerStorage, targetStorage);

            TaskUtils.runLater(() -> entity.setHealth(20), 3);
        }
    }

    @EventHandler
    public void onEntityDamageByChildEntityEvent(EntityDamageByChildEntityEvent ev) {
        if (ev.getChild() instanceof EntityArrow && ev.getEntity() instanceof Player) {
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

            if (shooter.getUniqueId().equals(entity.getUniqueId())) {
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

            if (playerStorage.attack(shooter)) {
                entity.sendMessage(KitPvP.replacePlaceholders("NEW_ATTACK", "<player>", shooter.getName()));
            }

            if (targetStorage.attack(entity)) {
                shooter.sendMessage(KitPvP.replacePlaceholders("NEW_ATTACK", "<player>", entity.getName()));
            }

            float dmg = entity.getHealth() - ev.getFinalDamage();

            if (dmg < 1.0) {
                ev.setCancelled(true);

                handleDeath(entity, shooter, playerStorage, targetStorage);

                TaskUtils.runLater(() -> entity.setHealth(20), 3);
            }

            return;
        }

        ev.setCancelled(true);
    }

    private boolean cancelAttack(PlayerStorage playerStorage, Player target) {
        Player lastAttack = playerStorage.getLastAttack();

        return lastAttack != null && !lastAttack.equals(target);
    }

    private void handleDeath(Player player, Player killer, PlayerStorage playerStorage, PlayerStorage targetStorage) {
        String message = KitPvP.replacePlaceholders("PLAYER_KILLED_BY", "<player>", player.getName(), "<killer>", killer.getName());

        KitPvP.defaultValues(player, null);

        targetStorage.increaseKills();
        targetStorage.increaseCoins(KitPvP.getInstance().getConfig().getInt("coins.kill-won", 1));
        TaskUtils.runAsync(() -> MysqlProvider.getInstance().savePlayerStorage(targetStorage));

        playerStorage.decreaseCoins(KitPvP.getInstance().getConfig().getInt("coins.death-lost", 1));
        playerStorage.death();

        if (RankFactory.getInstance().tryUpdateRank(targetStorage)) {
            killer.sendMessage(KitPvP.replacePlaceholders("NEW_RANK", "<new_rank>", targetStorage.getRankName()));
        }

        targetStorage.attack(null);
        playerStorage.attack(null);

        for (Player target : player.getLevel().getPlayers().values()) {
            target.sendMessage(message);
        }
    }
}