package org.vansama.weatheraddon;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class SaltListener implements Listener {
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final Random random;
    private final Logger logger;

    public SaltListener(final JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.random = new Random();
        this.logger = Logger.getLogger("Minecraft");
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState() && config.getBoolean("salt-snow.enabled")) {
            scheduleSaltSnow(event.getWorld());
        }
    }

    private void scheduleSaltSnow(World world) {
        try {
            int minInterval = config.getInt("salt-snow.min-interval") * 1200; // 转换为游戏内刻数
            int maxInterval = config.getInt("salt-snow.max-interval") * 1200;
            int duration = config.getInt("salt-snow.duration") * 20; // 转换为游戏内刻数

            int interval = random.nextInt((maxInterval - minInterval) / 20 + 1) * 20 + minInterval;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                world.setStorm(true); // 开始下雪
                Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                    for (Player player : world.getPlayers()) {
                        // 发送警告消息
                        player.sendMessage(config.getString("salt-snow.warning-message").replace("&", "§"));
                        // 降低玩家的饱食度
                        int newHunger = player.getFoodLevel() - config.getInt("salt-snow.hunger-decrease");
                        player.setFoodLevel(Math.max(0, newHunger));
                    }
                }, 0, 20); // 每刻钟执行一次

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    world.setStorm(false); // 结束下雪
                }, duration); // 设置盐雪持续时间
            }, interval);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "安排盐雪时发生错误", e);
        }
    }
}