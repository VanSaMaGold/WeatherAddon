package org.vansama.weatheraddon;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class WeatherAddon extends JavaPlugin {

    @Override
    public void onEnable() {
        // 获取日志记录器
        Logger logger = getLogger();

        // 加载默认配置并重载
        saveDefaultConfig();
        reloadConfig();

        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new SaltListener(this), this);
        getServer().getPluginManager().registerEvents(new SourListener(this), this);
        getServer().getPluginManager().registerEvents(new FireListener(this), this);

        // 注册命令监听器
        getCommand("weatheraddon").setExecutor(new CommandListener(this));
        getCommand("wa").setExecutor(new CommandListener(this));

        // 打印插件启用信息
        logger.info(ChatColor.GREEN + "----------    -----------");
        logger.info(ChatColor.GREEN + "-                  -");
        logger.info(ChatColor.GREEN + "-                  -");
        logger.info(ChatColor.GREEN + "-                  -");
        logger.info(ChatColor.GREEN + "----------         -");
        logger.info(ChatColor.GREEN + "");
        logger.info(ChatColor.AQUA + "WeatherAddon " + getDescription().getVersion() + " 版本 成功开启 by VanSaMa");
    }

    @Override
    public void onDisable() {
        reloadConfig();
        saveDefaultConfig();

        // 插件禁用时的日志信息
        getLogger().info(ChatColor.GREEN + "----------    -----------");
        getLogger().info(ChatColor.GREEN + "-                  -");
        getLogger().info(ChatColor.GREEN + "-                  -");
        getLogger().info(ChatColor.GREEN + "-                  -");
        getLogger().info(ChatColor.GREEN + "----------         -");
        getLogger().info(ChatColor.GREEN + "");
        getLogger().info(ChatColor.RED + "WeatherAddon " + getDescription().getVersion() + " 版本 关闭成功");
    }

    // 其他方法（如有必要）可以在这里添加
}