package tests.common.buildcraft.core.inventory;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import buildcraft.core.inventory.StackMergeHelper;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ItemStack.class)
public class StackMergeHelperTests {

	@Before
	public void setUp(){
		
	}
	
	@Test
	public void canStacksMerge_Stack1IsNull_ReturnsFalse(){
		
		ItemStack stack2 = mock(ItemStack.class);
		
		StackMergeHelper cut = new StackMergeHelper();
		boolean actual = cut.canStacksMerge(null, stack2);
		
		assertFalse(actual);
	}
	
	@Test
	public void canStacksMerge_Stack2IsNull_ReturnsFalse(){
		
		ItemStack stack1 = mock(ItemStack.class);
		
		StackMergeHelper cut = new StackMergeHelper();
		boolean actual = cut.canStacksMerge(stack1, null);
		
		assertFalse(actual);
	}
	
	@Test
	public void canStacksMerge_ItemsDiffer_ReturnsFalse(){
		
		ItemStack stack1 = mock(ItemStack.class);
		ItemStack stack2 = mock(ItemStack.class);
		
		when(stack1.isItemEqual(stack2)).thenReturn(false);
		
		StackMergeHelper cut = new StackMergeHelper();
		boolean actual = cut.canStacksMerge(stack1, stack2);
		
		assertFalse(actual);
	}
	
	@Test
	public void canStacksMerge_TagsDiffer_ReturnsFalse(){
		
		ItemStack stack1 = mock(ItemStack.class);
		ItemStack stack2 = mock(ItemStack.class);
		
		when(stack1.isItemEqual(stack2)).thenReturn(true);
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual(stack1, stack2)).thenReturn(false);
		
		StackMergeHelper cut = new StackMergeHelper();
		boolean actual = cut.canStacksMerge(stack1, stack2);
		
		assertFalse(actual);
	}
	
	@Test
	public void canStacksMerge_ItemAndTagsMatch_ReturnsTrue(){
		
		ItemStack stack1 = mock(ItemStack.class);
		ItemStack stack2 = mock(ItemStack.class);
		
		when(stack1.isItemEqual(stack2)).thenReturn(true);
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual(stack1, stack2)).thenReturn(true);
		
		StackMergeHelper cut = new StackMergeHelper();
		boolean actual = cut.canStacksMerge(stack1, stack2);
		
		assertTrue(actual);
	}
	
	@Test
	public void mergeStacks_StacksCantMerge_Returns0(){
		StackMergeHelper cut = new StackMergeHelper();
		int actual = cut.mergeStacks(null, null, false);
		assertEquals(0, actual);
	}
	
	@Test
	public void mergeStacks_TargetStackFull_Returns0(){
		ItemStack stack1 = mock(ItemStack.class);
		ItemStack stack2 = mock(ItemStack.class);
		stack1.stackSize = 1;
		stack2.stackSize = 64;
		when(stack1.isItemEqual(stack2)).thenReturn(true);
		when(stack2.getMaxStackSize()).thenReturn(64);
		
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual(stack1, stack2)).thenReturn(true);
		
		StackMergeHelper cut = new StackMergeHelper();
		int actual = cut.mergeStacks(stack1, stack2, false);
		assertEquals(0, actual);
	}
	
	@Test
	public void mergeStacks_TargetStackHasRoom_ReturnsAvailableRoom(){
		ItemStack stack1 = mock(ItemStack.class);
		ItemStack stack2 = mock(ItemStack.class);
		stack1.stackSize = 1;
		stack2.stackSize = 63;
		when(stack1.isItemEqual(stack2)).thenReturn(true);
		when(stack2.getMaxStackSize()).thenReturn(64);
		
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual(stack1, stack2)).thenReturn(true);
		
		StackMergeHelper cut = new StackMergeHelper();
		int actual = cut.mergeStacks(stack1, stack2, false);
		assertEquals(1, actual);
	}
	
	@Test
	public void mergeStacks_TargetStackHasRoomAndDoMerge_Stack2IsFilled(){
		ItemStack stack1 = mock(ItemStack.class);
		ItemStack stack2 = mock(ItemStack.class);
		stack1.stackSize = 1;
		stack2.stackSize = 63;
		when(stack1.isItemEqual(stack2)).thenReturn(true);
		when(stack2.getMaxStackSize()).thenReturn(64);
		
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual(stack1, stack2)).thenReturn(true);
		
		StackMergeHelper cut = new StackMergeHelper();
		int actual = cut.mergeStacks(stack1, stack2, true);
		assertEquals(64, stack2.stackSize);
	}
	
	@Test
	public void mergeStacks_TargetStackHasRoomAndDontMerge_Stack2IsNotModified(){
		ItemStack stack1 = mock(ItemStack.class);
		ItemStack stack2 = mock(ItemStack.class);
		stack1.stackSize = 1;
		stack2.stackSize = 63;
		when(stack1.isItemEqual(stack2)).thenReturn(true);
		when(stack2.getMaxStackSize()).thenReturn(64);
		
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual(stack1, stack2)).thenReturn(true);
		
		StackMergeHelper cut = new StackMergeHelper();
		int actual = cut.mergeStacks(stack1, stack2, false);
		assertEquals(63, stack2.stackSize);
	}
	
	@Test
	public void mergeStacks_TargetStackHasMoreRoomThanInput_ReturnsInput(){
		ItemStack stack1 = mock(ItemStack.class);
		ItemStack stack2 = mock(ItemStack.class);
		stack1.stackSize = 1;
		stack2.stackSize = 1;
		when(stack1.isItemEqual(stack2)).thenReturn(true);
		when(stack2.getMaxStackSize()).thenReturn(64);
		
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual(stack1, stack2)).thenReturn(true);
		
		StackMergeHelper cut = new StackMergeHelper();
		int actual = cut.mergeStacks(stack1, stack2, false);
		assertEquals(1, actual);
	}

}
