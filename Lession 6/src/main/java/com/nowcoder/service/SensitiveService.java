package com.nowcoder.service;

import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class SensitiveService implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveService.class);

    /**
     * 默认敏感词替换符
     */
    private static final String DEFAULT_REPLACEMENT = "敏感词";

    // 私有类,一个敏感词字符就是一个节点
    private class TrieNode {

        /**
         * true 关键词的终结 ； false 继续
         */
        private boolean end = false;

        /**
         * key下一个字符，value是对应的节点
         */
        // 一个节点的下一个节点可能有多个，所以用一个容器来装所有的下个节点
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        /**
         * 向指定位置添加节点树
         */
        // key是什么呢？？？实际上就是个敏感词字符（char类型），节点只有一个变量end
        void addSubNode(Character key, TrieNode node) {
            subNodes.put(key, node);
        }

        /**
         * 获取下个节点
         */
        // key是当前节点的子节点的key
        TrieNode getSubNode(Character key) {
            return subNodes.get(key);
        }

        boolean isKeywordEnd() {
            return end;
        }

        // 设置当前节点的end属性
        void setKeywordEnd(boolean end) {
            this.end = end;
        }

        public int getSubNodeCount() {
            return subNodes.size();
        }


    }


    /**
     * 根节点
     */
    // 下面就开始构造私有树
    private TrieNode rootNode = new TrieNode();


    /**
     * 判断是否是一个符号
     */
    private boolean isSymbol(char c) {
        int ic = (int) c;
        // 0x2E80-0x9FFF 东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (ic < 0x2E80 || ic > 0x9FFF);
    }


    /**
     * 过滤敏感词
     */
    // 核心过滤敏感词算法
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return text;
        }
        String replacement = DEFAULT_REPLACEMENT;
        StringBuilder result = new StringBuilder();

        TrieNode tempNode = rootNode;
        int begin = 0; // 回滚数
        int position = 0; // 当前比较的位置

        while (position < text.length()) {
            char c = text.charAt(position);
            // 判断是否是有效输入文本字符
            if (isSymbol(c)) {
                if (tempNode == rootNode) {
                    result.append(c);
                    ++begin;
                }
                ++position;
                continue;
            }

            tempNode = tempNode.getSubNode(c);

            // 当前位置的匹配结束
            if (tempNode == null) {
                // 以begin开始的字符串不存在敏感词
                result.append(text.charAt(begin));
                // 跳到下一个字符开始测试
                position = begin + 1;
                begin = position;
                // 回到树初始节点
                tempNode = rootNode;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词， 从begin到position的位置用replacement替换掉
                result.append(replacement);
                position = position + 1;
                begin = position;
                tempNode = rootNode;
            } else {
                // 继续在树上查找，不需要对可以敏感字符串进行result的处理
                ++position;
            }
        }
        // 把最后一个检测的类似敏感的”字符串“加上
        result.append(text.substring(begin));

        return result.toString();
    }

    private void addWord(String lineTxt) {
        TrieNode tempNode = rootNode;
        // 循环每个敏感词中的每一个字符
        for (int i = 0; i < lineTxt.length(); ++i) {
            Character c = lineTxt.charAt(i);
            // 过滤空格
            if (isSymbol(c)) {
                continue;
            }
            
            // rootNode与其subNode；或者父节点与子节点怎么连接的呢，实际上是在一个TriNode中就定义了某一节点的子节点：用getSubNode获取
            TrieNode node = tempNode.getSubNode(c);

            if (node == null) { // 子节点为空，则加入目前的字符；比如abc,abcj都是敏感词，j继续是c的子节点，c、j都要被设为敏感词
                node = new TrieNode();
                tempNode.addSubNode(c, node);
            }

            //当前节点变为父节点
            tempNode = node;

            if (i == lineTxt.length() - 1) {
                // 关键词结束， 设置结束标志
                tempNode.setKeywordEnd(true);
            }
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        rootNode = new TrieNode();

        try {
            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(read);
            String lineTxt;
            while ((lineTxt = bufferedReader.readLine()) != null) {
                lineTxt = lineTxt.trim();
                addWord(lineTxt);
            }
            read.close();
        } catch (Exception e) {
            logger.error("读取敏感词文件失败" + e.getMessage());
        }
    }

    public static void main(String[] argv) {
        SensitiveService s = new SensitiveService();
        s.addWord("色情");
        s.addWord("好色");
        System.out.print(s.filter("你好X色**情XX"));
    }
}
