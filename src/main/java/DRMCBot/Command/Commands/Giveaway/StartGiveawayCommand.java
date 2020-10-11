package DRMCBot.Command.Commands.Giveaway;

import DRMCBot.CacheList;
import DRMCBot.Command.CommandContext;
import DRMCBot.Command.ICommand;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartGiveawayCommand implements ICommand {

    private EventWaiter waiter;

    private List<String> toReact = new LinkedList<>();

    Pattern timeFormat = Pattern.compile("(?:(\\d{1,5})(h|s|m|d))+?");

    private String toReactGiveawayEmoji = "\uD83C\uDF89";

    public StartGiveawayCommand(EventWaiter waiter) {
        this.waiter = waiter;
    }

    @Override
    public void handle(CommandContext ctx) {
        if (!ctx.getSelfMember().hasPermission(Permission.MESSAGE_WRITE)) {
            return;
        }

        if (ctx.getArgs().isEmpty() || ctx.getArgs().size() < 3) {
            ctx.getChannel().sendMessage("請輸入足夠參數！").queue();
            return;
            //drmc!startgiveaway 2h3s 3 test
        }

        String giveawayLength = ctx.getArgs().get(0);


        try {
            Integer.parseInt(ctx.getArgs().get(1));
        } catch (Exception e) {
            ctx.getChannel().sendMessage("請輸入抽出人數！").queue();
            return;
        }

        boolean ifMatches = Pattern.matches(String.valueOf(timeFormat), giveawayLength);
        if (!ifMatches) {
            ctx.getChannel().sendMessage("時間格式輸入不正確！").queue();
            return;
        }

        toReact.add("\u2705");
        toReact.add("\u274e");

        EmbedBuilder embedBuilder = EmbedUtils.defaultEmbed()
                .setTitle("請注意！確定舉辦？")
                .setDescription("此機器人因處於開發階段，開發員會經常關閉bot而造成有時抽獎結束開獎時bot無法開獎而造成您必須自行開獎，若您無法接受，請使用其他機器人開獎！");
        ctx.getChannel().sendMessage(embedBuilder.build()).queue(
                message -> {
                    message.addReaction(toReact.get(0)).queue();
                    message.addReaction(toReact.get(1)).queue();
                    waiter.waitForEvent(
                            GuildMessageReactionAddEvent.class,
                            event -> {
                                MessageReaction.ReactionEmote emote = event.getReactionEmote();
                                User user = event.getUser();

                                return !user.isBot() && event.getMessageIdLong() == message.getIdLong() && toReact.contains(emote.getName());
                            },
                            event -> {
                                if (toReact.indexOf(event.getReactionEmote().getName()) == 0) {
                                    //TODO:啟動抽獎
                                    //drmc!startgiveaway 2h3s 3 test
                                    Matcher matcher = timeFormat.matcher(giveawayLength);
                                    long sec = 0;
                                    String winnercount;
                                    winnercount = ctx.getArgs().get(1);
                                    String price = ctx.getMessage().getContentRaw().substring(ctx.getMessage().getContentRaw().indexOf(ctx.getArgs().get(2)));
                                    while (matcher.find()) {
                                        switch (matcher.group(2)) {
                                            case "d" -> sec += (Integer.parseInt(matcher.group(1)) * 86400);
                                            case "h" -> sec += (Integer.parseInt(matcher.group(1)) * 3600);
                                            case "m" -> sec += (Integer.parseInt(matcher.group(1)) * 60);
                                            case "s" -> sec += Integer.parseInt(matcher.group(1));
                                        }
                                    }
                                    message.delete().queue();
                                    new GiveawayRunner(sec, ctx.getChannel(), Integer.parseInt(winnercount), price, ctx.getMember());
                                } else {
                                    message.delete().queue();
                                    ctx.getChannel().sendMessage("抽獎已為您取消！").queue();
                                    return;
                                }
                            },
                            30, TimeUnit.SECONDS,
                            () -> {
                                ctx.getChannel().sendMessage("30秒超過！").queue();
                            }
                    );
                }
        );
    }

    public class GiveawayRunner {
        public long Second;
        public TextChannel GiveawayMessageChannel;
        public Message GiveawayMessage;
        public String price;
        public int winnerCount;
        public boolean toRoll = true;
        public Member GiveawayCreator;
        public String CreatedDay;
        public String EndDay;

        public GiveawayRunner(long second, TextChannel channel, int winnerCount, String price, Member creator) {
            this.Second = second;
            this.GiveawayMessageChannel = channel;
            this.winnerCount = winnerCount;
            this.price = price;
            this.GiveawayCreator = creator;
            this.Start();
        }

        public void Start() {
            long d = Second / 86400;
            long h = (Second % 86400) / 3600;
            long m = ((Second % 86400) % 3600) / 60;
            long s = ((Second % 86400) % 3600) % 60;
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(ZoneId.of("Asia/Taipei")));
            CreatedDay = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
            calendar.add(Calendar.SECOND, (int) Second);
            EndDay = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime()));
            EmbedBuilder embedBuilder = EmbedUtils.defaultEmbed()
                    .setTitle(":tada: 抽獎已開始！")
                    .setDescription("**抽獎獎項：**" + price + "\n剩餘時間：" + d + "天" + h + "小時" + m + "分" + s + "秒")
                    .setFooter("由" + GiveawayCreator.getUser().getAsTag() + "舉辦\n" + "將結束於：" + EndDay + " (GMT+08:00)", GiveawayCreator.getUser().getAvatarUrl());
            GiveawayMessage = GiveawayMessageChannel.sendMessage(embedBuilder.build()).complete();
            GiveawayMessage.addReaction(toReactGiveawayEmoji).complete();
            CacheList.RunningGiveaway.put(GiveawayMessageChannel.getId() + GiveawayMessage.getId(), Second);
            new Thread(this::Run).start();
        }

        public void Run() {
            //TODO:倒計時、確認抽獎是否被終止
            while (Second > 0) {
                Second -= 1;
                if (Second % 15 == 0 && Second != 0) {
                    long d = Second / 86400;
                    long h = (Second % 86400) / 3600;
                    long m = ((Second % 86400) % 3600) / 60;
                    long s = ((Second % 86400) % 3600) % 60;
                    EmbedBuilder embedBuilder = EmbedUtils.defaultEmbed()
                            .setTitle(":tada: 抽獎已開始！")
                            .setDescription("**抽獎獎項：**" + price + "\n剩餘時間：" + d + "天" + h + "小時" + m + "分" + s + "秒")
                            .setFooter("由" + GiveawayCreator.getUser().getAsTag() + "舉辦\n" + "將結束於：" + EndDay + " (GMT+08:00)", GiveawayCreator.getUser().getAvatarUrl());
                    GiveawayMessage.editMessage(embedBuilder.build()).queue();
                }
                if (!CacheList.RunningGiveaway.containsKey(GiveawayMessageChannel.getId() + GiveawayMessage.getId())) {
                    toRoll = false;
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            Roll();
        }

        public void Roll() {
            if (toRoll) {
                long d = Second / 86400;
                long h = (Second % 86400) / 3600;
                long m = ((Second % 86400) % 3600) / 60;
                long s = ((Second % 86400) % 3600) % 60;
                EmbedBuilder embedBuilder = EmbedUtils.defaultEmbed()
                        .setTitle(":tada: 抽獎已圓滿結束！")
                        .setDescription("**抽獎獎項：**" + price + "\n剩餘時間：" + d + "天" + h + "小時" + m + "分" + s + "秒")
                        .setFooter("由" + GiveawayCreator.getUser().getAsTag() + "舉辦\n" + "已結束於：" + EndDay + " (GMT+08:00)", GiveawayCreator.getUser().getAvatarUrl())
                        .setColor(0x0DFC3D);
                GiveawayMessage.editMessage(embedBuilder.build()).queue();

                GiveawayMessageChannel.sendMessage("抽獎完成").queue();
            }
        }
    }

    @Override
    public String getName() {
        return "startgiveaway";
    }
}
