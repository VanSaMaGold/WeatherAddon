package org.vansama.weatheraddon;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.Random;

public final class FireListener implements Listener {
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final Random random;

    public FireListener(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.random = new Random();
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState() && config.getBoolean("fire-rain.enabled")) {
            int chance = config.getInt("fire-rain.chance");
            if (random.nextInt(100) < chance) { // 根据配置的几率来触发火焰雨
                startFireRain(event.getWorld());
            }
        }
    }

    private void startFireRain(org.bukkit.World world) {
        int duration = config.getInt("fire-rain.duration") * 20; // 转换为游戏内刻数

        // 发送警告消息
        for (Player player : world.getPlayers()) {
            player.sendMessage(config.getString("fire-rain.warning-message").replace("&", "§"));
        }

        // 给予火焰效果，设置为水无法扑灭
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Player player : world.getPlayers()) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, duration, 0, false, false));
            }
        });

        // 结束火焰雨效果
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : world.getPlayers()) {
                player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            }
        }, duration);

        // 设置天气为下雨
        world.setStorm(true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> world.setStorm(false), duration);
    }
}