package dev.thatsmybaby.command.kit.subcommand;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import dev.thatsmybaby.KitPvP;
import dev.thatsmybaby.TaskUtils;
import dev.thatsmybaby.command.PlayerSubCommand;
import dev.thatsmybaby.kit.Kit;
import dev.thatsmybaby.kit.KitFactory;
import dev.thatsmybaby.provider.MysqlProvider;
import dev.thatsmybaby.provider.PlayerStorage;

public class BuySubCommand extends PlayerSubCommand {

    public BuySubCommand(String name, String permission) {
        super(name, permission);
    }

    @Override
    public void execute(Player player, String label, String[] args) {
        if (args.length < 1) {
            player.sendMessage(TextFormat.RED + "Usage: /kit buy <kit_name>");

            return;
        }

        Kit kit = KitFactory.getInstance().getKit(args[0]);

        if (kit == null) {
            player.sendMessage(TextFormat.RED + "Kit " + args[0] + " not found");

            return;
        }

        PlayerStorage playerStorage = PlayerStorage.of(player);

        if (playerStorage.getKits().contains(kit.getKitName())) {
            player.sendMessage(KitPvP.replacePlaceholders("KIT_ALREADY_UNLOCKED", "<kit_name>", kit.getKitName()));

            return;
        }

        if (kit.getPrice() > playerStorage.getCoins()) {
            player.sendMessage(KitPvP.replacePlaceholders("YOU_DONT_HAVE_ENOUGH_COINS", "<coins>", String.valueOf(playerStorage.getCoins()), "<coins_need>", String.valueOf(kit.getPrice())));

            return;
        }

        TaskUtils.runAsync(() -> {
            MysqlProvider.getInstance().unlockKit(player.getName(), kit.getKitName());

            playerStorage.decreaseCoins(kit.getPrice());
            playerStorage.getKits().add(kit.getKitName());

            player.sendMessage(KitPvP.replacePlaceholders("KIT_PURCHASED", "<kit_name>", kit.getKitName(), "<price>", String.valueOf(kit.getPrice())));
        });
    }
}