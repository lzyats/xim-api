package com.platform.modules.auth.util;

import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.ArrayList; // 添加ArrayList导入
import java.util.Collections;
import java.util.List;

@Component
public class XianxiaNameGenerator {

    // 天玄类（大气磅礴）- 50字
    private static final List<Character> HEAVENLY_CHARS = Arrays.asList(
            '天', '玄', '霄', '穹', '昊', '宸', '星', '月', '日', '辰',
            '霄', '云', '风', '雷', '电', '雨', '虹', '霞', '雾', '霭',
            '宇', '宙', '空', '虚', '灵', '幻', '霄', '汉', '苍', '穹',
            '乾', '坤', '罡', '煞', '曜', '晖', '芒', '曦', '晷', '垣',
            '玑', '衡', '璇', '霄', '霆', '雳', '霭', '雯', '霓', '霭'
    );

    // 灵物类（自然灵气）- 50字
    private static final List<Character> SPIRITUAL_CHARS = Arrays.asList(
            '灵', '仙', '神', '魔', '妖', '鬼', '魂', '魄', '丹', '符',
            '剑', '琴', '棋', '书', '画', '镜', '珠', '玉', '宝', '器',
            '法', '咒', '诀', '印', '箓', '幡', '铃', '钟', '鼎', '炉',
            '芝', '草', '药', '丹', '液', '晶', '髓', '血', '脉', '络',
            '罡', '煞', '雷', '火', '风', '水', '土', '金', '木', '灵'
    );

    // 山水类（意境悠远）- 50字
    private static final List<Character> LANDSCAPE_CHARS = Arrays.asList(
            '山', '峰', '崖', '谷', '川', '河', '湖', '海', '溪', '泉',
            '石', '岩', '洞', '潭', '瀑', '涧', '岛', '洲', '沙', '滩',
            '岭', '峦', '壑', '岗', '坡', '崖', '矶', '礁', '峡', '峪',
            '渊', '池', '溏', '泽', '洼', '漠', '野', '原', '丘', '陵',
            '岳', '岱', '嵩', '衡', '华', '恒', '泰', '岷', '庐', '巫'
    );

    // 草木类（清新灵秀）- 50字
    private static final List<Character> PLANT_CHARS = Arrays.asList(
            '松', '柏', '竹', '梅', '兰', '菊', '荷', '莲', '桃', '李',
            '杏', '桂', '枫', '桐', '桑', '花', '草', '藤', '芝', '药',
            '荼', '蘼', '薇', '蔷', '薇', '茉', '莉', '芙', '蓉', '蔷',
            '薇', '萱', '蕙', '芷', '蘅', '菱', '芡', '芰', '蒲', '苇',
            '蕨', '茅', '荻', '蒿', '芩', '芪', '芎', '芍', '苓', '杞'
    );

    // 心境类（超凡脱俗）- 50字
    private static final List<Character> MIND_CHARS = Arrays.asList(
            '清', '净', '空', '寂', '悟', '觉', '明', '虚', '无', '真',
            '诚', '善', '忍', '慈', '悲', '喜', '乐', '静', '修', '行',
            '思', '念', '忆', '情', '欲', '痴', '嗔', '怨', '恨', '悔',
            '悟', '禅', '定', '慧', '空', '明', '灵', '性', '心', '神',
            '魂', '魄', '志', '意', '念', '觉', '悟', '通', '晓', '彻'
    );

    // 动作类（仙法神通）- 50字
    private static final List<Character> ACTION_CHARS = Arrays.asList(
            '飞', '翔', '游', '行', '坐', '卧', '炼', '修', '悟', '参',
            '祭', '唤', '引', '聚', '散', '化', '凝', '破', '立', '降',
            '诛', '灭', '镇', '封', '禁', '解', '渡', '劫', '炼', '铸',
            '驭', '御', '操', '纵', '控', '制', '守', '护', '卫', '保',
            '攻', '击', '刺', '斩', '劈', '砍', '削', '破', '碎', '裂'
    );

    private static final List<List<Character>> ALL_CATEGORIES = Arrays.asList(
            HEAVENLY_CHARS, SPIRITUAL_CHARS, LANDSCAPE_CHARS,
            PLANT_CHARS, MIND_CHARS, ACTION_CHARS
    );

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 生成随机长度(4-8)的仙侠风格名字
     */
    public static String generateXianxiaName() {
        int length = 4 + RANDOM.nextInt(5);
        StringBuilder name = new StringBuilder(length);

        if (length <= 5) {
            List<List<Character>> twoCategories = pickTwoCategories();
            int part1 = length / 2;
            appendRandomChars(name, part1, twoCategories.get(0));
            appendRandomChars(name, length - part1, twoCategories.get(1));
        } else {
            List<List<Character>> threeCategories = pickThreeCategories();
            appendRandomChars(name, 2, threeCategories.get(0));
            appendRandomChars(name, 2, threeCategories.get(1));
            appendRandomChars(name, length - 4, threeCategories.get(2));
        }

        return name.toString();
    }

    private static void appendRandomChars(StringBuilder sb, int count, List<Character> category) {
        for (int i = 0; i < count; i++) {
            sb.append(category.get(RANDOM.nextInt(category.size())));
        }
    }

    private static List<List<Character>> pickTwoCategories() {
        List<List<Character>> shuffled = new ArrayList<>(ALL_CATEGORIES);
        Collections.shuffle(shuffled, RANDOM);
        return shuffled.subList(0, 2);
    }

    private static List<List<Character>> pickThreeCategories() {
        List<List<Character>> shuffled = new ArrayList<>(ALL_CATEGORIES);
        Collections.shuffle(shuffled, RANDOM);
        return shuffled.subList(0, 3);
    }
}