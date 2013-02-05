package tests.common.buildcraft.core.inventory;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.*;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeDirection;
import buildcraft.core.inventory.TransactorSimple;
import buildcraft.core.utils.SimpleInventory;

@RunWith(PowerMockRunner.class)
@PrepareForTest(ItemStack.class)
public class TransactorSimpleTests {
	private IInventory fakeInventory;
	private ItemStack fakeStack;
	private ItemStack injectStack;
	
	@Before
	public void setUp(){
		fakeInventory = mock(IInventory.class);
		fakeStack = mock(ItemStack.class);
		injectStack = mock(ItemStack.class);
		
		when(fakeInventory.getInventoryStackLimit()).thenReturn(64);
		
		when(injectStack.getMaxStackSize()).thenReturn(64);
		when(injectStack.copy()).thenReturn(mock(ItemStack.class));
		
		when(fakeStack.copy()).thenReturn(mock(ItemStack.class));
		
	}
	
	@Test
	public void inject_inventoryHasNoSlots_returns0(){
		when(fakeInventory.getSizeInventory()).thenReturn(0);
				
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		int actual = cut.inject(injectStack, ForgeDirection.UNKNOWN, false);
		
		assertEquals(0, actual);
	}
	
	@Test
	public void inject_inventoryIsEmptyDontAdd_returns64(){
		when(fakeInventory.getSizeInventory()).thenReturn(1);
		when(fakeInventory.getStackInSlot(0)).thenReturn(null);
		injectStack.stackSize = 64;
		
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		int actual = cut.inject(injectStack, ForgeDirection.UNKNOWN, false);
		
		assertEquals(64, actual);
	}
	
	@Test
	public void inject_inventoryIsEmptyDoAdd_returns64(){
		when(fakeInventory.getSizeInventory()).thenReturn(1);
		when(fakeInventory.getStackInSlot(0)).thenReturn(null);
		injectStack.stackSize = 64;
		
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		int actual = cut.inject(injectStack, ForgeDirection.UNKNOWN, true);
		
		assertEquals(64, actual);
	}
	
	@Test
	public void inject_inventoryIsEmptyDontAdd_InventoryIsNotSet(){
		when(fakeInventory.getSizeInventory()).thenReturn(1);
		when(fakeInventory.getStackInSlot(0)).thenReturn(null);
		injectStack.stackSize = 64;
		
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		cut.inject(injectStack, ForgeDirection.UNKNOWN, false);
		verify(fakeInventory, never()).setInventorySlotContents(eq(0), (ItemStack) anyObject());
	}
	
	@Test
	public void inject_inventoryIsEmptyDoAdd_InventoryIsSet(){
		when(fakeInventory.getSizeInventory()).thenReturn(1);
		when(fakeInventory.getStackInSlot(0)).thenReturn(null);
		injectStack.stackSize = 64;
		
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		cut.inject(injectStack, ForgeDirection.UNKNOWN, true);
		verify(fakeInventory).setInventorySlotContents(eq(0), (ItemStack) anyObject());
	}
	
	@Test
	public void inject_inventoryIsFull_returns0(){
		when(fakeInventory.getSizeInventory()).thenReturn(0);
		when(fakeInventory.getStackInSlot(0)).thenReturn(fakeStack);
		
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		
		int actual = cut.inject(injectStack, ForgeDirection.UNKNOWN, false);
		assertEquals(0, actual);
	}
	
	@Test
	public void inject_inventoryIsHalfFullOverFlowDontAdd_returns1(){
		ItemStack stack1 = mock(ItemStack.class);
		when(stack1.isItemEqual((ItemStack) anyObject())).thenReturn(true);
		when(stack1.getMaxStackSize()).thenReturn(64);
		stack1.stackSize = 63;
		
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual((ItemStack)anyObject(), (ItemStack)anyObject())).thenReturn(true);
		when(fakeInventory.getSizeInventory()).thenReturn(1);
		when(fakeInventory.getStackInSlot(0)).thenReturn(stack1);
		injectStack.stackSize = 64;
		
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		int actual = cut.inject(injectStack, ForgeDirection.UNKNOWN, false);
		assertEquals(1, actual);
	}
	
	@Test
	public void inject_inventoryIsHalfFullOverFlowDoAdd_returns1(){
		ItemStack stack1 = mock(ItemStack.class);
		when(stack1.isItemEqual((ItemStack) anyObject())).thenReturn(true);
		when(stack1.getMaxStackSize()).thenReturn(64);
		stack1.stackSize = 63;
		
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual((ItemStack)anyObject(), (ItemStack)anyObject())).thenReturn(true);
		when(fakeInventory.getSizeInventory()).thenReturn(1);
		when(fakeInventory.getStackInSlot(0)).thenReturn(stack1);
		injectStack.stackSize = 64;
		
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		int actual = cut.inject(injectStack, ForgeDirection.UNKNOWN, true);
		assertEquals(1, actual);
	}

	@Test
	public void inject_inventoryIsHalfFullWrongItem_returns0(){
		ItemStack stack1 = mock(ItemStack.class);
		when(stack1.isItemEqual((ItemStack) anyObject())).thenReturn(false);
		when(stack1.getMaxStackSize()).thenReturn(64);
		stack1.stackSize = 63;
		
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual((ItemStack)anyObject(), (ItemStack)anyObject())).thenReturn(true);
		when(fakeInventory.getSizeInventory()).thenReturn(1);
		when(fakeInventory.getStackInSlot(0)).thenReturn(stack1);
		injectStack.stackSize = 64;
		
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		int actual = cut.inject(injectStack, ForgeDirection.UNKNOWN, true);
		assertEquals(0, actual);
	}
	
	@Test
	public void inject_inventoryIsHalfFullWrongItemTag_returns0(){
		ItemStack stack1 = mock(ItemStack.class);
		when(stack1.isItemEqual((ItemStack) anyObject())).thenReturn(true);
		when(stack1.getMaxStackSize()).thenReturn(64);
		stack1.stackSize = 63;
		
		PowerMockito.mockStatic(ItemStack.class);
		when(ItemStack.areItemStackTagsEqual((ItemStack)anyObject(), (ItemStack)anyObject())).thenReturn(false);
		when(fakeInventory.getSizeInventory()).thenReturn(1);
		when(fakeInventory.getStackInSlot(0)).thenReturn(stack1);
		injectStack.stackSize = 64;
		
		TransactorSimple cut = new TransactorSimple(fakeInventory);
		int actual = cut.inject(injectStack, ForgeDirection.UNKNOWN, true);
		assertEquals(0, actual);
	}
	

}
