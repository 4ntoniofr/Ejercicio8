package avl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Created with IntelliJ IDEA. User: Antonio J. Nebro Date: 09/07/13 Time: 15:29
 */
@DisplayName("Given an AVL node")
public class AvlNodeTest {

    private AvlNode<Integer> node;

    @BeforeEach
    public void setUp() throws Exception {
        node = new AvlNode<Integer>(5);
    }

    @AfterEach
    public void tearDown() throws Exception {
        node = null;
    }



    @Test
    public void testHasLeft() {
        assertFalse(node.hasLeft());
        AvlNode<Integer> node2 = new AvlNode<Integer>(6);
        node.setLeft(node2);
        assertTrue(node.hasLeft());
    }

    @Test
    public void testHasRight() {
        assertFalse(node.hasRight());
        AvlNode<Integer> node2 = new AvlNode<Integer>(6);
        node.setRight(node2);
        assertTrue(node.hasRight());
    }



    @Test
    @DisplayName("When height is set, returns same height")
    public void getHeight_WhenHeightIsNotValid_ThrowsException() {
        int expectedHeight =  1000213;
        ReflectionTestUtils.setField(node, "height", expectedHeight);

        Assertions.assertThat(node.getHeight())
                .isEqualTo(expectedHeight);
    }
}
