package me.nov.zelixkiller.utils;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodNode;

public class MethodUtils {
	public static Map<LabelNode, LabelNode> getLabelMap(InsnList list) {
		Map<LabelNode, LabelNode> map = new HashMap<>();
		for (AbstractInsnNode ain : list.toArray()) {
			if (ain instanceof LabelNode) {
				map.put((LabelNode) ain, new LabelNode());
			}
		}
		return map;
	}

	/**
	 * copy InsnList start included, end excluded
	 * 
	 * @param list
	 * @param start
	 * @param end
	 * @return
	 */
	public static InsnList copy(InsnList list, AbstractInsnNode start, AbstractInsnNode end) {
		InsnList newList = new InsnList();
		Map<LabelNode, LabelNode> labelMap = getLabelMap(list);
		AbstractInsnNode ain = start == null ? list.getFirst() : start;
		while (ain != null && ain != end) {
			newList.add(ain.clone(labelMap));
			ain = ain.getNext();
		}
		return newList;
	}

	public static MethodNode cloneInstructions(MethodNode method, String newName) {
		MethodNode mn = new MethodNode(method.access, newName != null ? newName : method.name, method.desc,
				method.signature, method.exceptions.toArray(new String[0]));
		mn.instructions.add(copy(method.instructions, null, null));
		mn.maxLocals = method.maxLocals;
		mn.maxStack = method.maxStack;
		return mn;
	}
}
