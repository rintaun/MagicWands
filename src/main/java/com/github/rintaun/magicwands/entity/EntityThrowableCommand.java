package com.github.rintaun.magicwands.entity;

import com.github.rintaun.magicwands.command.IMagicWandsCommandSender;
import com.github.rintaun.magicwands.command.MagicWandsCommandHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityThrowableCommand extends EntityThrowable implements IMagicWandsCommandSender
{
    private String tickCommand = "";
    private int tickDelay = 0;
    private int tickInterval = 1;
    private String impactCommand = "";
    private float velocity = 1.5f;
    private float gravity = 0.03f;
    private float inaccuracy = 1.0f;
    private float headingX = 0.0f;
    private float headingY = 0.0f;
    private float headingZ = 0.0f;

    private int ticksSinceStart = 0;
    private int ticksSinceLast = 0;
    private NBTTagCompound lastCommand;
    private NBTTagCompound currentCommand;

    public EntityThrowableCommand(World worldIn)
    {
        super(worldIn);
    }

    public EntityThrowableCommand(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
    }

    public EntityThrowableCommand(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    public boolean delaying()
    {
        return this.ticksSinceStart < (this.tickDelay + 1);
    }

    public boolean tickCommandReady()
    {
        return (this.ticksSinceLast - this.tickInterval) == 0;
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();
        if (!this.worldObj.isRemote)
        {
            if (this.isDead || this.tickInterval == 0)
            {
                return;
            }
            this.ticksSinceStart++;
            if (this.delaying())
            {
                return;
            }
            this.ticksSinceLast++;
            if (this.tickCommandReady())
            {
                this.ticksSinceLast = 0;
                this.setCurrentCommand(this.tickCommand);
                this.runCommand();
            }
        }
    }

    protected void onImpact(MovingObjectPosition impact)
    {
        if (!this.worldObj.isRemote)
        {
            this.setCurrentCommand(this.impactCommand);
            this.runCommand();
            this.setDead();
        }
    }

    public NBTTagCompound getCommandData()
    {
        return this.currentCommand;
    }

    public void setCurrentCommand(String command)
    {
        this.currentCommand = new NBTTagCompound();
        this.currentCommand.setString("Command", command);
    }

    public void runCommand()
    {
        //MagicWandsCommandHandler commandHandler = new MagicWandsCommandHandler(this.worldObj, this.getPosition(), this);
        //commandHandler.execute();
        this.lastCommand = this.currentCommand;
        this.currentCommand = null;
    }

    @Override
    protected float getVelocity()
    {
        return this.velocity;
    }

    @Override
    protected float getInaccuracy()
    {
        return this.inaccuracy;
    }

    @Override
    protected float getGravityVelocity()
    {
        return this.gravity;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        NBTTagCompound userData;
        if (tagCompound.hasKey("Tags"))
        {
            userData = tagCompound.getCompoundTag("Tags");
        }
        else {
            userData = new NBTTagCompound();
        }

        userData.setString("tickCommand", this.tickCommand);
        userData.setString("impactCommand", this.impactCommand);
        userData.setInteger("tickDelay", this.tickDelay);
        userData.setInteger("tickInterval", this.tickInterval);
        userData.setFloat("velocity", this.velocity);
        userData.setFloat("gravity", this.gravity);
        userData.setFloat("inaccuracy", this.inaccuracy);

        NBTTagCompound heading = new NBTTagCompound();
        heading.setFloat("x", this.headingX);
        heading.setFloat("y", this.headingY);
        heading.setFloat("z", this.headingZ);

        userData.setTag("heading", heading);
        userData.setTag("lastCommand", this.lastCommand);

        tagCompound.setTag("Tags", userData);
        tagCompound.setInteger("ticksSinceLast", this.ticksSinceLast);

    }

    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);
        NBTTagCompound userData = tagCompound.getCompoundTag("Tags");
        if (userData != null)
        {
            this.tickCommand = userData.getString("tickCommand");
            this.impactCommand = userData.getString("impactCommand");
            this.tickDelay = userData.getInteger("tickDelay");
            this.tickInterval = userData.getInteger("tickInterval");
            this.velocity = userData.getFloat("velocity");
            this.gravity = userData.getFloat("gravity");
            this.inaccuracy = userData.getFloat("inaccuracy");

            NBTTagCompound heading = tagCompound.getCompoundTag("heading");
            if (heading != null)
            {
                this.headingX = heading.getFloat("x");
                this.headingY = heading.getFloat("y");
                this.headingZ = heading.getFloat("z");
            }

            this.lastCommand = tagCompound.getCompoundTag("lastCommand");
        }

        this.ticksSinceLast = tagCompound.getInteger("ticksSinceLast");
    }
}

