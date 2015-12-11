package zmaster587.advancedRocketry.asm;

import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {


	private static final String CLASS_KEY_ENTITYRENDERER = "net.minecraft.client.renderer.EntityRenderer";
	private static final String CLASS_KEY_ENTITYLIVEINGBASE = "net.minecraft.entity.EntityLivingBase";
	private static final String CLASS_KEY_ENTITYLIVINGRENDERER = "net.minecraft.client.renderer.entity.RenderLivingEntity";
	private static final String CLASS_KEY_ENTITY = "net.minecraft.entity.Entity";
	private static final String CLASS_KEY_ENTITY_PLAYER_SP = "net.minecraft.client.entity.EntityPlayerSP";
	private static final String CLASS_KEY_ENTITY_PLAYER_MP = "net.minecraft.client.entity.EntityPlayerMP";
	private static final String CLASS_KEY_ENTITY_PLAYER = "net.minecraft.entity.player.EntityPlayer";
	private static final String CLASS_KEY_NETHANDLERPLAYSERVER = "net.minecraft.network.NetHandlerPlayServer";
	private static final String CLASS_KEY_C03PACKETPLAYER = "net.minecraft.network.play.client.C03PacketPlayer";
	private static final String CLASS_KEY_WORLD = "net.minecraft.world.World";
	private static final String CLASS_KEY_BLOCK = "net.minecraft.block.Block";
	private static final String METHOD_KEY_PROCESSPLAYER = "processPlayer";
	private static final String METHOD_KEY_JUMP = "jump";
	private static final String METHOD_KEY_MOVEENTITY = "moveEntity";
	private static final String METHOD_KEY_SETPOSITION = "setPosition";
	private static final String METHOD_KEY_ONLIVINGUPDATE = "net.minecraft.client.entity.EntityPlayerSP.onLivingUpdate";
	private static final String METHOD_KEY_GETLOOKVEC = "net.minecraft.entity.EntityLivingBase.getLookVec";
	private static final String METHOD_KEY_DORENDER  = "net.minecraft.client.renderer.entity.RenderLivingEntity.doRender";
	private static final String METHOD_KEY_MOVEENTITYWITHHEADING = "net.minecraft.entity.EntityLivingBase.moveEntityWithHeading";
	private static final String METHOD_KEY_MOVEFLYING = "net.minecraft.entity.Entity.moveFlying";
	private static final String METHOD_KEY_SETBLOCK = CLASS_KEY_WORLD + ".setBlock";
	private static final String METHOD_KEY_SETBLOCKMETADATAWITHNOTIFY = CLASS_KEY_WORLD + ".setBlockMetadataWithNotify";
	
	private static final String FIELD_YAW = "net.minecraft.client.renderer.EntityRenderer.rotationYaw";
	private static final String FIELD_PITCH = "net.minecraft.client.renderer.EntityRenderer.rotationPitch";
	private static final String FIELD_PREV_YAW = "net.minecraft.client.renderer.EntityRenderer.prevRotationYaw";
	private static final String FIELD_PREV_PITCH = "net.minecraft.client.renderer.EntityRenderer.prevRotationPitch";
	private static final String FIELD_PLAYERENTITY = "net.minecraft.network.NetHandlerPlayServer.playerEntity";
	private static final String FIELD_HASMOVED = "net.minecraft.network.NetHandlerPlayServer.hasMoved";
	private static final HashMap<String, SimpleEntry<String, String>> entryMap = new HashMap<String, SimpleEntry<String, String>>();


	private boolean obf;

	/*private class ClassEntry {
		String name, obfName, desc;

		public ClassEntry(String name, String obfName, String desc) {
			this.name = name;
			this.obfName = obfName;
			this.desc = desc;
		}

		public String getObfName() {return obfName; }
		public String getDeobfName() {return name; }
		public String getDesc() {return desc;}
	}*/

	public ClassTransformer() {
		//TODO: obf names
		entryMap.put(CLASS_KEY_ENTITYRENDERER, new SimpleEntry<String, String>("net/minecraft/client/renderer/EntityRenderer", "blt"));
		entryMap.put(CLASS_KEY_ENTITYLIVEINGBASE, new SimpleEntry<String, String>("net/minecraft/entity/EntityLivingBase", ""));
		entryMap.put(CLASS_KEY_ENTITYLIVINGRENDERER, new SimpleEntry<String, String>("net/minecraft/client/renderer/entity/RendererLivingEntity", ""));
		entryMap.put(CLASS_KEY_ENTITY, new SimpleEntry<String, String>("net/minecraft/entity/Entity",""));
		entryMap.put(CLASS_KEY_ENTITY_PLAYER_SP, new SimpleEntry<String, String>("net/minecraft/client/entity/EntityPlayerSP",""));
		entryMap.put(CLASS_KEY_ENTITY_PLAYER_MP, new SimpleEntry<String, String>("net/minecraft/entity/player/EntityPlayerMP",""));
		entryMap.put(CLASS_KEY_ENTITY_PLAYER, new SimpleEntry<String, String>("net/minecraft/entity/player/EntityPlayer",""));
		entryMap.put(CLASS_KEY_NETHANDLERPLAYSERVER, new SimpleEntry<String, String>("net/minecraft/network/NetHandlerPlayServer",""));
		entryMap.put(CLASS_KEY_C03PACKETPLAYER, new SimpleEntry<String, String>("net/minecraft/network/play/client/C03PacketPlayer",""));
		entryMap.put(CLASS_KEY_WORLD, new SimpleEntry<String, String>("net/minecraft/world/World","ahb"));
		entryMap.put(CLASS_KEY_BLOCK, new SimpleEntry<String, String>("net/minecraft/block/Block","aij"));

		entryMap.put(METHOD_KEY_PROCESSPLAYER, new SimpleEntry<String, String>("processPlayer",""));
		entryMap.put(METHOD_KEY_MOVEENTITY, new SimpleEntry<String, String>("moveEntity",""));
		entryMap.put(METHOD_KEY_SETPOSITION, new SimpleEntry<String, String>("setPosition",""));
		entryMap.put(METHOD_KEY_GETLOOKVEC, new SimpleEntry<String, String>("getLook", ""));
		entryMap.put(METHOD_KEY_DORENDER, new SimpleEntry<String, String>("doRender",""));
		entryMap.put(METHOD_KEY_MOVEENTITYWITHHEADING, new SimpleEntry<String, String>("moveEntityWithHeading",""));
		entryMap.put(METHOD_KEY_MOVEFLYING, new SimpleEntry<String, String>("moveFlying",""));
		entryMap.put(METHOD_KEY_ONLIVINGUPDATE, new SimpleEntry<String, String>("onLivingUpdate",""));
		entryMap.put(METHOD_KEY_JUMP, new SimpleEntry<String, String>("jump",""));
		entryMap.put(METHOD_KEY_SETBLOCK, new SimpleEntry<String, String>("setBlock", "d"));
		entryMap.put(METHOD_KEY_SETBLOCKMETADATAWITHNOTIFY, new SimpleEntry<String, String>("setBlockMetadataWithNotify", "a"));

		entryMap.put(FIELD_YAW, new SimpleEntry<String, String>("rotationYaw", "blt"));
		entryMap.put(FIELD_PITCH, new SimpleEntry
				<String, String>("rotationPitch", "blt"));
		entryMap.put(FIELD_PREV_YAW, new SimpleEntry<String, String>("prevRotationYaw", "blt"));
		entryMap.put(FIELD_PREV_PITCH, new SimpleEntry<String, String>("prevRotationPitch", "blt"));
		entryMap.put(FIELD_PLAYERENTITY, new SimpleEntry<String, String>("playerEntity", ""));
		entryMap.put(FIELD_HASMOVED, new SimpleEntry<String, String>("hasMoved", ""));
	}

	@Override
	public byte[] transform(String name, String transformedName,
			byte[] bytes) {


		//Vanilla deobf
		String changedName = name.replace('.','/');

		//Need to override setPosition to fix bounding boxes

		/*if(changedName.equals(getName(CLASS_KEY_NETHANDLERPLAYSERVER))) {
			ClassNode cn = startInjection(bytes);
			MethodNode processPlayer = getMethod(cn, "setPlayerLocation", "(DDDFF)V");

			if(processPlayer != null ) {
				final InsnList nodeAdd = new InsnList();

				AbstractInsnNode pos = null;


				for (int i = processPlayer.instructions.size()-1; i > 0; i--) {
					AbstractInsnNode ain = processPlayer.instructions.get(i);

					if(ain.getOpcode() == Opcodes.ALOAD) {
						pos = ain;
					}
				} 


				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_NETHANDLERPLAYSERVER), getName(FIELD_PLAYERENTITY), "L" + getName(CLASS_KEY_ENTITY_PLAYER_MP) + ";"));
				nodeAdd.add(new VarInsnNode(Opcodes.DLOAD, 1));
				nodeAdd.add(new VarInsnNode(Opcodes.DLOAD, 3));
				nodeAdd.add(new VarInsnNode(Opcodes.DLOAD, 5));
				nodeAdd.add(new VarInsnNode(Opcodes.FLOAD, 7));
				nodeAdd.add(new VarInsnNode(Opcodes.FLOAD, 8));
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_NETHANDLERPLAYSERVER), getName(FIELD_PLAYERENTITY), "L" + getName(CLASS_KEY_ENTITY_PLAYER_MP) + ";"));
				nodeAdd.add(new TypeInsnNode(Opcodes.CHECKCAST, getName(CLASS_KEY_ENTITYLIVEINGBASE)));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));

				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC,  "zmaster587/advancedRocketry/client/ClientHelper", "netHandlerSetPlayerLocation", "(L" + getName(CLASS_KEY_ENTITY_PLAYER_MP) + ";DDDFFI)V", false));
				nodeAdd.add(new InsnNode(Opcodes.RETURN));

				processPlayer.instructions.insertBefore(processPlayer.instructions.getFirst(), nodeAdd);
			}

			return finishInjection(cn);
		}
		if(changedName.equals(getName(CLASS_KEY_ENTITY))) {
			ClassNode cn = startInjection(bytes);
			MethodNode setPosition = getMethod(cn, getName(METHOD_KEY_SETPOSITION), "(DDD)V");

			if(setPosition != null) {
				final InsnList nodeAdd = new InsnList();
				final LabelNode jumpNode = new LabelNode();

				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD,0));
				nodeAdd.add(new TypeInsnNode(Opcodes.INSTANCEOF, getName(CLASS_KEY_ENTITYLIVEINGBASE)));
				nodeAdd.add(new JumpInsnNode(Opcodes.IFEQ, jumpNode));

				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD,0));
				nodeAdd.add(new TypeInsnNode(Opcodes.CHECKCAST, getName(CLASS_KEY_ENTITYLIVEINGBASE)));
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD,0));
				nodeAdd.add(new TypeInsnNode(Opcodes.CHECKCAST, getName(CLASS_KEY_ENTITYLIVEINGBASE)));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
				nodeAdd.add(new VarInsnNode(Opcodes.DLOAD, 1));
				nodeAdd.add(new VarInsnNode(Opcodes.DLOAD, 3));
				nodeAdd.add(new VarInsnNode(Opcodes.DLOAD, 5));
				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "setPosition", "(L" + getName(CLASS_KEY_ENTITYLIVEINGBASE) + ";IDDD)V", false));
				nodeAdd.add(jumpNode);

				setPosition.instructions.insertBefore(setPosition.instructions.getLast().getPrevious(), nodeAdd);
			}

			return finishInjection(cn);
		}
		if(changedName.equals(getDeobfName(CLASS_KEY_ENTITY_PLAYER_SP))) {
			ClassNode cn = startInjection(bytes);
			MethodNode onLivingUpdate = getMethod(cn, getName(METHOD_KEY_ONLIVINGUPDATE), "()V");

			if(onLivingUpdate != null) 
			{
				final InsnList nodeAdd = new InsnList();
				final LabelNode label = new LabelNode();
				final LabelNode endOfInvokeLabel = new LabelNode();

				AbstractInsnNode pos = null;
				AbstractInsnNode gotoPos = null;
				int eqnum = 13;


				for (int i = 0; i < onLivingUpdate.instructions.size(); i++) {
					AbstractInsnNode ain = onLivingUpdate.instructions.get(i);

					if(ain.getOpcode() == Opcodes.IFEQ && eqnum-- == 0) {
						pos = ain;
						for(i=i+1, eqnum = 4; i < onLivingUpdate.instructions.size(); i++) {
							//eqnum = 2;
							ain = onLivingUpdate.instructions.get(i);
							if(ain.getOpcode() == Opcodes.ALOAD && eqnum-- == 0) {
								gotoPos = ain;
								break;
							}
						}
						break;
					}
				}

				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD,0));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
				nodeAdd.add(new JumpInsnNode(Opcodes.IFEQ, endOfInvokeLabel));
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "moveFlyingVerticalOverride", "(L" + getName(CLASS_KEY_ENTITY_PLAYER_SP) + ";I)V", false));
				nodeAdd.add(new JumpInsnNode(Opcodes.GOTO, label));
				nodeAdd.add(endOfInvokeLabel);

				onLivingUpdate.instructions.insert(pos, nodeAdd);

				onLivingUpdate.instructions.insertBefore(gotoPos, label);

			}

			return finishInjection(cn);
		}
		if(changedName.equals(getDeobfName(CLASS_KEY_ENTITYLIVINGRENDERER))) {
			ClassNode cn = startInjection(bytes);

			MethodNode doRender = getMethod(cn, getName(METHOD_KEY_DORENDER), "(L" + getName(CLASS_KEY_ENTITYLIVEINGBASE)+";DDDFF)V");

			if(doRender != null) {
				final InsnList nodeAdd = new InsnList();
				AbstractInsnNode pos = null;
				int eqnum = 2;

				for (int i = 0; i < doRender.instructions.size(); i++) {
					AbstractInsnNode ain = doRender.instructions.get(i);

					if(ain.getOpcode() == Opcodes.IFEQ && eqnum-- == 0) {
						for(i = i - 1; i > 0; i--) {
							ain = doRender.instructions.get(i);
							if(ain.getOpcode() == Opcodes.FSTORE) {
								pos = ain;
								break;
							}
						}
						break;
					}
				}


				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 1));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));

				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 1));

				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "transformEntity", "(IL" + getName(CLASS_KEY_ENTITYLIVEINGBASE) + ";)V", false));

				doRender.instructions.insert(pos, nodeAdd);
			}

			return finishInjection(cn);
		}
		if(changedName.equals(getDeobfName(CLASS_KEY_ENTITYLIVEINGBASE))) {
			ClassNode cn = startInjection(bytes);

			MethodNode moveFlying = new MethodNode(Opcodes.ACC_PUBLIC, getName(METHOD_KEY_MOVEFLYING), "(FFF)V", null, null);
			MethodNode moveEntity = new MethodNode(Opcodes.ACC_PUBLIC, getName(METHOD_KEY_MOVEENTITY), "(DDD)V", null, null);
			MethodNode setPosition = new MethodNode(Opcodes.ACC_PUBLIC, getName(METHOD_KEY_SETPOSITION), "(DDD)V", null, null);


			//Add need to override setPosition in entitybase to fix collision boxes
			final InsnList moveEntityNode = new InsnList();
			final LabelNode jumpToMoveEntity = new LabelNode();
			final LabelNode jumpToEndEntity = new LabelNode();

			moveEntityNode.add(new VarInsnNode(Opcodes.ALOAD, 0));
			moveEntityNode.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
			moveEntityNode.add(new JumpInsnNode(Opcodes.IFNE, jumpToMoveEntity));
			moveEntityNode.add(new VarInsnNode(Opcodes.ALOAD, 0));
			moveEntityNode.add(new VarInsnNode(Opcodes.DLOAD, 1));
			moveEntityNode.add(new VarInsnNode(Opcodes.DLOAD, 3));
			moveEntityNode.add(new VarInsnNode(Opcodes.DLOAD, 5));
			moveEntityNode.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, getName(CLASS_KEY_ENTITY), getName(METHOD_KEY_MOVEENTITY), "(DDD)V", false));
			moveEntityNode.add(new JumpInsnNode(Opcodes.GOTO, jumpToEndEntity));

			moveEntityNode.add(jumpToMoveEntity);
			moveEntityNode.add(new VarInsnNode(Opcodes.ALOAD, 0));
			moveEntityNode.add(new VarInsnNode(Opcodes.ALOAD, 0));
			moveEntityNode.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
			moveEntityNode.add(new VarInsnNode(Opcodes.DLOAD, 1));
			moveEntityNode.add(new VarInsnNode(Opcodes.DLOAD, 3));
			moveEntityNode.add(new VarInsnNode(Opcodes.DLOAD, 5));
			moveEntityNode.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "moveEntity", "(L" + getName(CLASS_KEY_ENTITYLIVEINGBASE) + ";IDDD)V", false));
			moveEntityNode.add(jumpToEndEntity);

			moveEntityNode.add(new InsnNode(Opcodes.RETURN));

			moveEntity.instructions.insert(moveEntityNode);
			cn.methods.add(moveEntity);

			//Add moveFlying methods nodes
			final InsnList moveFlyingNode = new InsnList();
			final LabelNode jumpTo = new LabelNode();
			final LabelNode endJump = new LabelNode();

			moveFlyingNode.add(new VarInsnNode(Opcodes.ALOAD, 0));
			moveFlyingNode.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
			moveFlyingNode.add(new JumpInsnNode(Opcodes.IFNE, jumpTo));
			moveFlyingNode.add(new VarInsnNode(Opcodes.ALOAD, 0));
			moveFlyingNode.add(new VarInsnNode(Opcodes.FLOAD, 1));
			moveFlyingNode.add(new VarInsnNode(Opcodes.FLOAD, 2));
			moveFlyingNode.add(new VarInsnNode(Opcodes.FLOAD, 3));
			moveFlyingNode.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, getName(CLASS_KEY_ENTITY), getName(METHOD_KEY_MOVEFLYING), "(FFF)V", false));
			moveFlyingNode.add(new JumpInsnNode(Opcodes.GOTO, endJump));
			moveFlyingNode.add(jumpTo);
			moveFlyingNode.add(new VarInsnNode(Opcodes.ALOAD, 0));
			moveFlyingNode.add(new VarInsnNode(Opcodes.ALOAD, 0));
			moveFlyingNode.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
			moveFlyingNode.add(new VarInsnNode(Opcodes.FLOAD, 1));
			moveFlyingNode.add(new VarInsnNode(Opcodes.FLOAD, 2));
			moveFlyingNode.add(new VarInsnNode(Opcodes.FLOAD, 3));
			moveFlyingNode.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "moveFlying", "(L" + getName(CLASS_KEY_ENTITYLIVEINGBASE) + ";IFFF)V", false));
			moveFlyingNode.add(endJump);
			moveFlyingNode.add(new InsnNode(Opcodes.RETURN));

			moveFlying.instructions.insert(moveFlyingNode);
			cn.methods.add(moveFlying);
			//End add moveFlying method nodes

			cn.fields.add(new FieldNode(Opcodes.ACC_PUBLIC, "gravRotation", Type.INT_TYPE.getDescriptor(), "I", new Integer(1)));

			//TODO: might break in obf
			MethodNode constructor = getMethod(cn, "<init>", "(Lnet/minecraft/world/World;)V");
			MethodNode getLook = getMethod(cn, getName(METHOD_KEY_GETLOOKVEC), "(F)Lnet/minecraft/util/Vec3;");
			MethodNode moveEntityWithHeading = getMethod(cn, getName(METHOD_KEY_MOVEENTITYWITHHEADING), "(FF)V");
			MethodNode jump = getMethod(cn, getName(METHOD_KEY_JUMP), "()V");
			if(constructor != null) {
				final InsnList nodeAdd = new InsnList();

				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new InsnNode(Opcodes.ICONST_1));
				nodeAdd.add(new FieldInsnNode(Opcodes.PUTFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));

				constructor.instructions.insertBefore(constructor.instructions.getLast(), nodeAdd);
			}
			if(jump != null) {
				final InsnList nodeAdd = new InsnList();
				LabelNode skipAddLabel = new LabelNode();

				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
				nodeAdd.add(new JumpInsnNode(Opcodes.IFEQ, skipAddLabel));
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD,0));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "livingEntityJump", "(IL" + getName(CLASS_KEY_ENTITYLIVEINGBASE) + ";)V", false));
				nodeAdd.add(new InsnNode(Opcodes.RETURN));
				nodeAdd.add(skipAddLabel);

				jump.instructions.insertBefore(jump.instructions.getFirst(), nodeAdd);
			}
			if(moveEntityWithHeading != null) {
				final InsnList nodeAdd = new InsnList();
				AbstractInsnNode pos = null, endAssign = null;
				LabelNode skipAddLabel = new LabelNode();
				LabelNode endEdit = new LabelNode();

				for (int i = moveEntityWithHeading.instructions.size()-1; i > 0; i--) {
					AbstractInsnNode ain = moveEntityWithHeading.instructions.get(i);

					if(ain.getOpcode() == Opcodes.GOTO) {
						for( i = i + 1; i < moveEntityWithHeading.instructions.size(); i++) {
							ain = moveEntityWithHeading.instructions.get(i);
							if(ain.getOpcode() == Opcodes.ALOAD && pos == null) {
								pos = ain;
							}
							if(ain.getOpcode() == Opcodes.PUTFIELD) {
								endAssign = ain;
								break;
							}
						}
						break;
					}
				}


				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD,0));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
				nodeAdd.add(new JumpInsnNode(Opcodes.IFEQ, skipAddLabel));
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD,0));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "transformGravity", "(IL" + getName(CLASS_KEY_ENTITYLIVEINGBASE) + ";)V", false));
				nodeAdd.add(new JumpInsnNode(Opcodes.GOTO, endEdit));
				nodeAdd.add(skipAddLabel);

				moveEntityWithHeading.instructions.insertBefore(pos, nodeAdd);
				moveEntityWithHeading.instructions.insert(endAssign, endEdit);
			}
			//Make look direction consistent with transformed camera
			if(getLook != null) {
				final InsnList nodeAdd = new InsnList();
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
				nodeAdd.add(new VarInsnNode(Opcodes.FLOAD, 1));

				//TODO: might break in obf
				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "createModifiedLookVector", "(L" + getName(CLASS_KEY_ENTITYLIVEINGBASE) +";IF)Lnet/minecraft/util/Vec3;", false));
				nodeAdd.add(new InsnNode(Opcodes.ARETURN));

				getLook.instructions.insertBefore(getLook.instructions.getFirst(), nodeAdd);

			}

			return finishInjection(cn);
		}

		//Transform Camera as needed
		if(changedName.equals(getDeobfName(CLASS_KEY_ENTITYRENDERER))) {
			ClassNode cn = startInjection(bytes);
			//TODO: obfuscated names
			MethodNode orientCamera = getMethod(cn, "orientCamera", "(F)V");
			MethodNode updateCameraAndRender = getMethod(cn, "updateCameraAndRender", "(F)V");

			if(orientCamera != null) {
				final InsnList nodeAdd = new InsnList();
				final InsnList nodeAdd2 = new InsnList();
				final InsnList nodeAdd3 = new InsnList();
				final InsnList nodeAdd4 = new InsnList();

				AbstractInsnNode pos = null;
				AbstractInsnNode pos2 = null;
				int ifneNum = 1;
				int invokeNum = 1;

				AbstractInsnNode pos3 = null;
				int gotoNum = 3;

				for (int i = 0; i < orientCamera.instructions.size(); i++) {
					AbstractInsnNode ain = orientCamera.instructions.get(i);
					if (ain.getOpcode() == Opcodes.IFNE && ifneNum-- == 0) {

						pos = ain;
					}

					if(pos != null && ain.getOpcode() == Opcodes.INVOKESTATIC && invokeNum-- == 0) {
						pos2 = ain;
					}
					if (ain.getOpcode() == Opcodes.GOTO && gotoNum-- == 0) {

						pos3 = ain;
					}
				}

				LabelNode gotoLabel = new LabelNode();
				LabelNode gotoLabel2 = new LabelNode();

				nodeAdd.add(new FieldInsnNode(Opcodes.GETSTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "rotate", "Z"));
				//nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
				//nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));
				nodeAdd.add(new JumpInsnNode(Opcodes.IFEQ, gotoLabel2));

				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));
				nodeAdd.add(new FieldInsnNode(Opcodes.GETFIELD, getName(CLASS_KEY_ENTITYLIVEINGBASE), "gravRotation", "I"));

				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 2));


				nodeAdd.add(new VarInsnNode(Opcodes.FLOAD,1));

				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "transformCamera2", "(IL" + getName(CLASS_KEY_ENTITYLIVEINGBASE) + ";F)V", false));
				nodeAdd.add(new JumpInsnNode(Opcodes.GOTO, gotoLabel));
				nodeAdd.add(gotoLabel2);


				nodeAdd3.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "transformCamera", "()V", false));
				nodeAdd4.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "transformCamera", "()V", false));

				orientCamera.instructions.insertBefore(orientCamera.instructions.get(orientCamera.instructions.indexOf(pos3)), nodeAdd3);
				orientCamera.instructions.insertBefore(orientCamera.instructions.get(orientCamera.instructions.indexOf(pos3)+8), nodeAdd4);

				orientCamera.instructions.insertBefore(orientCamera.instructions.get(orientCamera.instructions.indexOf(pos)-4), nodeAdd);
				orientCamera.instructions.insertBefore(orientCamera.instructions.get(orientCamera.instructions.indexOf(pos2)+1), gotoLabel);
				//TODO: OVerride Entity.setAngles
				//639
			}
			/*if(orientCamera != null) {
				final InsnList nodeAdd = new InsnList();
				final InsnList nodeAdd2 = new InsnList();

				AbstractInsnNode pos = null;
				int gotoNum = 3;

				for (int i = 0; i < orientCamera.instructions.size(); i++) {
					AbstractInsnNode ain = orientCamera.instructions.get(i);
					if (ain.getOpcode() == Opcodes.GOTO && gotoNum-- == 0) {

						pos = ain;
						break;
					}
				}

				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "transformCamera", "()V", false));
				nodeAdd2.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "transformCamera", "()V", false));

				orientCamera.instructions.insertBefore(orientCamera.instructions.get(orientCamera.instructions.indexOf(pos)), nodeAdd);
				orientCamera.instructions.insertBefore(orientCamera.instructions.get(orientCamera.instructions.indexOf(pos)+8), nodeAdd2);
				//TODO: OVerride Entity.setAngles

			}
			if(updateCameraAndRender != null) {
				final InsnList nodeAdd = new InsnList();

				AbstractInsnNode pos = null;
				int fmulNum = 4;

				for (int i = 0; i < updateCameraAndRender.instructions.size(); i++) {
					AbstractInsnNode ain = updateCameraAndRender.instructions.get(i);
					if (ain.getOpcode() == Opcodes.INVOKEVIRTUAL && fmulNum-- == 0) {

						pos = ain;
						break;
					}
				}
				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/client/ClientHelper", "transformMouse", "()V", false));

				updateCameraAndRender.instructions.insertBefore(updateCameraAndRender.instructions.get(updateCameraAndRender.instructions.indexOf(pos)+1), nodeAdd);

			}

			return finishInjection(cn);
		}*/

		if(changedName.equals(getName(CLASS_KEY_WORLD))) {
			ClassNode cn = startInjection(bytes);
			MethodNode setBlockMethod = getMethod(cn, "setBlock", "(IIIL" + getName(CLASS_KEY_BLOCK) +";II)Z");
			MethodNode setBlockMetaMethod = getMethod(cn, "setBlockMetadataWithNotify", "(IIIII)Z");
			
			if(setBlockMethod != null) {

				final InsnList nodeAdd = new InsnList();
				AbstractInsnNode pos = null;
				int fmulNum = 2;

				for (int i = setBlockMethod.instructions.size()-1; i >= 0 ; i--) {
					AbstractInsnNode ain = setBlockMethod.instructions.get(i);
					if (ain.getOpcode() == Opcodes.IRETURN && --fmulNum == 0) {
						pos = ain;
						break;
					}
				}


				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0));
				nodeAdd.add(new VarInsnNode(Opcodes.ILOAD, 1));
				nodeAdd.add(new VarInsnNode(Opcodes.ILOAD, 2));
				nodeAdd.add(new VarInsnNode(Opcodes.ILOAD, 3));
				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/api/atmosphere/AtmosphereHandler", "onBlockChange", "(L" + getName(CLASS_KEY_WORLD) + ";III)V", false));

				setBlockMethod.instructions.insertBefore(pos, nodeAdd);
			}
			
			if(setBlockMetaMethod != null) {

				final InsnList nodeAdd = new InsnList();
				AbstractInsnNode pos = null;
				int fmulNum = 2;

				for (int i = setBlockMetaMethod.instructions.size()-1; i >= 0 ; i--) {
					AbstractInsnNode ain = setBlockMetaMethod.instructions.get(i);
					if (ain.getOpcode() == Opcodes.IRETURN && --fmulNum == 0) {
						pos = ain;
						break;
					}
				}


				nodeAdd.add(new VarInsnNode(Opcodes.ALOAD, 0)); //this
				nodeAdd.add(new VarInsnNode(Opcodes.ILOAD, 1)); //x
				nodeAdd.add(new VarInsnNode(Opcodes.ILOAD, 2)); //y
				nodeAdd.add(new VarInsnNode(Opcodes.ILOAD, 3)); //z
				nodeAdd.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "zmaster587/advancedRocketry/api/atmosphere/AtmosphereHandler", "onBlockMetaChange", "(L" + getName(CLASS_KEY_WORLD) + ";III)V", false));

				setBlockMetaMethod.instructions.insertBefore(pos, nodeAdd);
			}
			return finishInjection(cn);
		}


		return bytes;
	}

	private ClassNode startInjection(byte[] bytes) {
		final ClassNode node = new ClassNode();
		final ClassReader reader = new ClassReader(bytes);
		reader.accept(node, 0);

		return node;
	}

	private byte[] finishInjection(ClassNode node) {
		final ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		node.accept(writer);
		return writer.toByteArray();
	}

	private MethodNode getMethod(ClassNode node, String name, String sig) {
		for(MethodNode methodNode : node.methods) {
			if(methodNode.name.equals(name) && methodNode.desc.equals(sig))
				return methodNode;
		}
		return null;
	}

	private String getName(String key) {
		SimpleEntry<String, String> entry = entryMap.get(key);
		if(entry == null)
			return "";
		else
			if(obf)
				return entry.getValue();
			else
				return entry.getKey();
	}


	private String getDeobfName(String key) {
		SimpleEntry<String, String> entry = entryMap.get(key);
		if(entry == null)
			return "";
		else
			return entry.getKey();
	}

}
