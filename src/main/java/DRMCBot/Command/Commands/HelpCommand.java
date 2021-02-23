package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class HelpCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        User botowner = ctx.getJDA().getUserById("470516498050580480");
        String[] x = {"discordinfo", "otherinfo", "generation", "entertainment", "music", "suggestion", "ticket", "management","giveaway"};
        List<String> category = Arrays.asList(x);
        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setAuthor(ctx.getJDA().getSelfUser().getName() + "#" + ctx.getJDA().getSelfUser().getDiscriminator(), null, ctx.getJDA().getSelfUser().getAvatarUrl())
                .setTitle("指令幫助");
        if (ctx.getArgs().isEmpty()) {
            embed.addField("查詢方法", "```help [類別]```", false)
                    .addField("類別選項", "**discordinfo**－Discord與機器人資訊類\n"
                            + "**otherinfo**－其他資訊類\n"
                            + "**generation**－生成工具類\n"
                            + "**entertainment**－娛樂類\n"
                            + "**music**－音樂播放類\n"
                            + "**suggestion**－建議類（目前僅限DRMC使用）\n"
                            + "**ticket**－管理員私訊窗口類（目前僅限DRMC使用）\n"
                            + "**management**－Discord與機器人資訊類\n"
                            + "**giveaway**－抽獎類（目前暫停使用）", false);
        } else if (category.contains(ctx.getArgs().get(0))) {
            switch (ctx.getArgs().get(0)) {
                case "discordinfo" -> {
                    embed.addField("Discord與機器人資訊類",
                            "``botinfo``（未完工）－此機器人的相關訊息\n"
                                    + "``help``－顯示此幫助\n"
                                    + "``ping``－取得此機器人回應速度\n"
                                    + "``serverinfo``－取得關於此伺服器之資訊\n"
                                    + "``uptime``－取得此機器人此次上線時間\n"
                                    + "``userinfo``－取得指定使用者之資料\n",
                            true);
                }
                case "otherinfo" -> {
                    embed.addField("其他資訊類",
                            "``accuweather``－取得指定位置天氣資訊\n"
                                    + "``couponleft``－查詢指定郵局振興三倍券剩餘數量\n"
                                    + "``instagram``－查詢指定Instagram使用者粉絲數等資訊\n"
                                    + "``minecraft``－查詢Minecraft使用者相關資訊\n",
                            true);
                }
                case "generation" -> {
                    embed.addField("生成工具類",
                            "``hulan``－生成一篇唬爛文\n" +
                                    "``qrcode``－生成QRCode\n"
                                    + "``reurl``－透過reurl.cc服務生成短網址\n"
                                    + "``tinyurl``－透過tinyurl.com服務生程短網址\n",
                            true);
                }
                case "entertainment" -> {
                    embed.addField("娛樂類",
                            "``joke``－顯示一則玩笑\n"
                                    + "``meme``－顯示一張迷因圖片\n"
                                    + "``animememe``－顯示一張動漫迷因圖片（暫停使用）\n"
                                    + "``chino``（未完工）－顯示一張智乃圖\n"
                                    + "``dog``－顯示一張狗的圖片\n"
                                    + "``cat``－顯示一張貓的圖片\n",
                            true);
                }
                case "music" -> {
                    embed.addField("音樂播放類",
                            "``join``－讓bot加入語音頻道\n"
                                    + "``leave``－讓bot離開語音頻道\n"
                                    + "``play``－播放一首音樂\n"
                                    + "``pause``－音樂暫停\n"
                                    + "``resume``－音樂繼續播放\n"
                                    + "``nowplaying``－查看目前播放之曲目\n"
                                    + "``resume``－音樂繼續播放\n"
                                    + "``queue``－查看播放列表\n"
                                    + "``skip``－跳過目前播放\n"
                                    + "``stop``－停止播放該歌曲並清空撥放列表\n",
                            true);
                }
                case "suggestion" -> {
                    embed.addField("建議類（目前僅限DRMC使用）",
                            "``suggest``－提一個建議\n"
                                    + "``implement``－將建議批改為「已實施」\n"
                                    + "``approve``－將建議批改為「已批准」\n"
                                    + "``consider``－將建議批改為「思考中」\n"
                                    + "``deny``－將建議批改為「已拒絕」\n",
                            true);
                }
                case "ticket" -> {
                    embed.addField("管理員私訊窗口類（目前僅限DRMC使用）",
                            "``openticket``－開啟管理員私訊窗口\n"
                                    + "``closeticket``－關閉管理員私訊窗口\n",
                            true);
                }
                case "management" -> {
                    embed.addField("伺服器管理類",
                            "``ban``－封鎖指定成員\n"
                                    + "``kick``－踢出指定成員\n"
                                    + "``clear``－清理指令數量訊息"
                                    + "``pinmessage``－釘選一則訊息\n"
                                    + "``unpinmessage``－解除釘選一則訊息\n"
                                    + "``setprefix``－設定機器人在此伺服器的前綴\n",
                            true);
                }
                case "giveaway" -> {
                    embed.addField("抽獎類",
                            "``startgiveaway``－舉辦一場抽獎\n"
                                    + "``rerollwinner``－重新抽出獲獎者\n"
                                    + "``endgiveaway``－取消抽獎",
                            true);
                }
            }
        } else {
            embed.addField("查詢方法", "```help [類別]```", false)
                    .addField("類別選項", "**discordinfo**－Discord與機器人資訊類\n"
                            + "**otherinfo**－其他資訊類\n"
                            + "**generation**－生成工具類\n"
                            + "**entertainment**－娛樂類\n"
                            + "**music**－音樂播放類\n"
                            + "**suggestion**－建議類（目前僅限DRMC使用）\n"
                            + "**ticket**－管理員私訊窗口類（目前僅限DRMC使用）\n"
                            + "**management**－Discord與機器人資訊類\n"
                            + "**giveaway**－抽獎類（目前暫停使用）", false);
        }
        embed.setFooter("機器人由「" + botowner.getName() + "#" + botowner.getDiscriminator() + "」製作", botowner.getAvatarUrl());
        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getCategory() {
        return "discordinfo";
    }

    @Override
    public String getdescription() {
        return null;
    }

    @Override
    public List<String> getUsages() {
        return null;
    }

    @Override
    public List<String> getExamples() {
        return null;
    }

    @Override
    public HashMap<String, HashMap<String, String>> getArguments() {
        return null;
    }
}
