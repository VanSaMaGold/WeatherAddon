package org.vansama.weatheraddon;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class SourListener implements Listener {
    private final JavaPlugin plugin;
    private final FileConfiguration config;
    private final Random random;

    public SourListener(JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        this.random = new Random();
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        // 检查酸雨是否启用
        if (!event.toWeatherState() && config.getBoolean("acid-rain.enabled")) {
            scheduleAcidRain(event.getWorld());
        }
    }

    private void scheduleAcidRain(World world) {
        try {
            int minInterval = config.getInt("acid-rain.min-interval") * 60 * 20; // 转换为游戏内刻数
            int maxInterval = config.getInt("acid-rain.max-interval") * 60 * 20;
            int duration = config.getInt("acid-rain.duration") * 20; // 转换为游戏内刻数

            int interval = random.nextInt(maxInterval - minInterval + 1) + minInterval;

            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!world.hasStorm()) {
                        world.setStorm(true);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Player player : world.getPlayers()) {
                                    player.sendMessage(config.getString("acid-rain.warning-message"));
                                }
                            }
                        }.runTaskLater(plugin, 20); // 延迟1秒发送警告消息

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                world.setStorm(false);
                            }
                        }.runTaskLater(plugin, duration); // 设置酸雨持续时间
                    }
                }
            }.runTaskLater(plugin, interval);
        } catch (Exception e) {
            plugin.getLogger().severe("发生错误：" + e.getMessage());
        }
    }
}