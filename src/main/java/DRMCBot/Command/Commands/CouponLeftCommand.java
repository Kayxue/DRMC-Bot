package DRMCBot.Command.Commands;

import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.natanbc.reliqua.util.StatusCodeValidator;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;

public class CouponLeftCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        if (ctx.getArgs().isEmpty()) {
            ctx.getChannel().sendMessage("請輸入查詢之郵局！").queue();
        }

        String input = ctx.getArgs().get(0);

        WebUtils.ins.getJSONArray("https://3000.gov.tw/hpgapi-openmap/api/getPostData",
                (builder) -> builder.setStatusCodeValidator(StatusCodeValidator.ACCEPT_200)
        ).async(
                jsonNodes -> {
                    String contains = "";
                        for (JsonNode jsonNode : jsonNodes) {
                            String storeName = jsonNode.get("storeNm").asText().substring(0, jsonNode.get("storeNm").asText().indexOf("("));
                            if (storeName.equals(input)) {
                                EmbedBuilder embedBuilder=EmbedUtils.getDefaultEmbed()
                                        .setTitle("查詢振興三倍券剩餘數量："+storeName)
                                        .setDescription("機構編號："+jsonNode.get("storeCd").asText())
                                        .addField("機構地址：","``"+jsonNode.get("zipCd").asText()+jsonNode.get("addr").asText()+"``",false)
                                        .addField("機構電話：","``"+jsonNode.get("tel").asText()+"``",false)
                                        .addField("營業時間：","``"+jsonNode.get("busiTime").asText().replaceAll("<br>"," ").replaceAll(" ","\n")+"``",false)
                                        .addField("振興券剩餘張數：","``"+jsonNode.get("total").asText()+"``",false)
                                        .setImage("https://api.mapbox.com/styles/v1/mapbox/light-v10/static/pin-s+01afef("+jsonNode.get("longitude").asText()+","+jsonNode.get("latitude").asText()+")/"+jsonNode.get("longitude").asText()+","+jsonNode.get("latitude").asText()+",18/800x512?access_token=pk.eyJ1Ijoia2F5eHVlIiwiYSI6ImNrZmpoeWFkbTBuOGMycWwzb2wyd2ZnbWgifQ.8NFY4VernJR-fc8CgdV4Hg")
                                        .setFooter("資料庫提供：政府資料開放平臺（data.gov.tw）\n資料更新時間："+jsonNode.get("updateTime").asText(),"https://2018h2odatacontest.github.io/info/img/gov_open_data.png");
                                ctx.getChannel().sendMessage(embedBuilder.build()).queue();
                                return;
                            } else if (storeName.contains(input)) {
                                contains += storeName+"（"+jsonNode.get("hsnNm").asText()+"）\n";
                            }
                        }
                    EmbedBuilder embed = EmbedUtils.getDefaultEmbed();
                    if (contains.length() == 0) {
                        embed.setTitle("沒有符合或相似的結果！")
                                .setDescription("嗯，真的。請再次檢查您輸入的內容");
                    } else {
                        embed.setTitle("沒有符合的結果！")
                                .setDescription("以下是名稱相似的地點，\n請看一下，說不定有你想找的。")
                        .addField("你可能想找的......","```"+contains+"```",false)
                                .setFooter("資料庫提供：政府資料開放平臺（data.gov.tw）","https://2018h2odatacontest.github.io/info/img/gov_open_data.png");
                    }
                    ctx.getChannel().sendMessage(embed.build()).queue();
                },
                error -> {
                    ctx.getChannel().sendMessage(error.getMessage()).queue();
                }
        );
    }



    @Override
    public String getName() {
        return "couponleft";
    }

    @Override
    public String getCategory() {
        return "otherinfo";
    }

    @Override
    public String getdescription() {
        return null;
    }

    @Override
    public EmbedBuilder gethelpembed() {
        return null;
    }
}
