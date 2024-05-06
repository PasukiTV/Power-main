package net.pasuki.power.blocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class BatteryBlock extends Block implements EntityBlock {

    public BatteryBlock() {
        super(Properties.of()
                .strength(3.5F)
                .requiresCorrectToolForDrops()
                .sound(SoundType.METAL));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new BatteryBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide) {
            return null;
        } else {
            return (lvl, pos, st, be) -> {
                if (be instanceof BatteryBlockEntity battery) {
                    battery.tickServer();
                }
            };
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.FACING, context.getNearestLookingDirection().getOpposite())
                .setValue(BlockStateProperties.POWERED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BlockStateProperties.POWERED, BlockStateProperties.FACING);
    }
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!worldIn.isClientSide() && hand == InteractionHand.MAIN_HAND && player.getItemInHand(hand).getItem() instanceof SwordItem) {
            BlockEntity blockEntity = worldIn.getBlockEntity(pos);
            if (blockEntity instanceof BatteryBlockEntity) {
                BatteryBlockEntity battery = (BatteryBlockEntity) blockEntity;
                Direction direction = hitResult.getDirection();

                // Hier rufe die entsprechende Methode auf, um die Ausgaberichtung zu ändern
                if (direction == Direction.UP) {
                    battery.toggleOutput(Direction.UP);
                    player.sendSystemMessage(Component.literal(battery.isOutput(Direction.UP) ? "Output oben aktiviert" : "Output oben deaktiviert"));
                } else if (direction == Direction.DOWN) {
                    battery.toggleOutput(Direction.DOWN);
                    player.sendSystemMessage(Component.literal(battery.isOutput(Direction.DOWN) ? "Output unten aktiviert" : "Output unten deaktiviert"));
                } else if (direction == Direction.EAST) {
                    battery.toggleOutput(Direction.EAST);
                    player.sendSystemMessage(Component.literal(battery.isOutput(Direction.EAST) ? "Output links aktiviert" : "Output links deaktiviert"));
                } else if (direction == Direction.SOUTH) {
                    battery.toggleOutput(Direction.SOUTH);
                    player.sendSystemMessage(Component.literal(battery.isOutput(Direction.SOUTH) ? "Output hinten aktiviert" : "Output hinten deaktiviert"));
                } else if (direction == Direction.WEST) {
                    battery.toggleOutput(Direction.WEST);
                    player.sendSystemMessage(Component.literal(battery.isOutput(Direction.WEST) ? "Output rechts aktiviert" : "Output rechts deaktiviert"));
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }




}
