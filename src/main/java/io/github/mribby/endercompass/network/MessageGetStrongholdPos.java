package io.github.mribby.endercompass.network;

import io.github.mribby.endercompass.EnderCompassMod;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageGetStrongholdPos implements IMessage, IMessageHandler<MessageGetStrongholdPos, IMessage> {
    public MessageGetStrongholdPos() {}

    @Override
    public void toBytes(ByteBuf buf) {}

    @Override
    public void fromBytes(ByteBuf buf) {}

    @Override
    public IMessage onMessage(MessageGetStrongholdPos message, MessageContext ctx) {
        final EntityPlayerMP player = ctx.getServerHandler().player;
        if (EnderCompassMod.containsCompass(player.inventory)) {
            final WorldServer world = (WorldServer) player.world;
            world.addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    BlockPos pos = world.getChunkProvider().getNearestStructurePos(world, "Stronghold", new BlockPos(player), false);
                    if (pos != null) {
                        EnderCompassMod.network.sendTo(new MessageSetStrongholdPos(pos), player);
                    }
                }
            });
        }
        return null;
    }
}
