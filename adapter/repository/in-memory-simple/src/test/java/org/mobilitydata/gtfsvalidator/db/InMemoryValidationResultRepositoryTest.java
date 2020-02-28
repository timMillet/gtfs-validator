/*
 * Copyright (c) 2020. MobilityData IO.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mobilitydata.gtfsvalidator.db;

import org.junit.jupiter.api.Test;
import org.mobilitydata.gtfsvalidator.usecase.notice.base.ErrorNotice;
import org.mobilitydata.gtfsvalidator.usecase.notice.base.InfoNotice;
import org.mobilitydata.gtfsvalidator.usecase.notice.base.Notice;
import org.mobilitydata.gtfsvalidator.usecase.notice.base.WarningNotice;
import org.mobilitydata.gtfsvalidator.usecase.port.ValidationResultRepository;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryValidationResultRepositoryTest {

    private static final String INFO_NOTICE_ID = "infoNoticeId";
    private static final String INFO_NOTICE_TITLE = "infoNoticeTitle";
    private static final String INFO_NOTICE_DESCRIPTION = "infoNoticeDescription";
    private static final String WARNING_NOTICE_ID = "warningNoticeId";
    private static final String ERROR_NOTICE_ID = "errorNoticeId";
    private static final String TEST_FILE_NAME = "test.tst";
    private static final String WARNING_NOTICE_TITLE = "warningNoticeTitle";
    private static final String WARNING_NOTICE_DESCRIPTION = "warningNoticeDescription";
    private static final String ERROR_NOTICE_TITLE = "errorNoticeTitle";
    private static final String ERROR_NOTICE_DESCRIPTION = "errorNoticeDescription";

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Test
    void addingNoticeShouldExtendNoticeList() {

        InfoNotice infoNotice = new InfoNotice(TEST_FILE_NAME, INFO_NOTICE_ID, INFO_NOTICE_TITLE,
                INFO_NOTICE_DESCRIPTION);

        WarningNotice warningNotice = new WarningNotice(TEST_FILE_NAME, WARNING_NOTICE_ID, WARNING_NOTICE_TITLE,
                WARNING_NOTICE_DESCRIPTION);

        ErrorNotice errorNotice = new ErrorNotice(TEST_FILE_NAME, ERROR_NOTICE_ID, ERROR_NOTICE_TITLE,
                ERROR_NOTICE_DESCRIPTION);

        ValidationResultRepository underTest = new InMemoryValidationResultRepository();

        underTest.addNotice(infoNotice);
        assertEquals(1, underTest.getAll().size());

        Notice testedNotice = underTest.getAll().stream()
                .filter(notice -> notice.getId().equals(infoNotice.getId()))
                .findAny()
                .get();

        assertThat(testedNotice, instanceOf(InfoNotice.class));

        underTest.addNotice(warningNotice);
        assertEquals(2, underTest.getAll().size());

        testedNotice = underTest.getAll().stream()
                .filter(notice -> notice.getId().equals(warningNotice.getId()))
                .findAny()
                .get();

        assertThat(testedNotice, instanceOf(WarningNotice.class));

        underTest.addNotice(errorNotice);
        assertEquals(3, underTest.getAll().size());

        testedNotice = underTest.getAll().stream()
                .filter(notice -> notice.getId().equals(errorNotice.getId()))
                .findAny()
                .get();

        assertThat(testedNotice, instanceOf(ErrorNotice.class));
    }

    @Test
    void callingGetAllMethodShouldReturnAllNotices() {

        WarningNotice warningNotice = new WarningNotice(TEST_FILE_NAME, WARNING_NOTICE_ID, WARNING_NOTICE_TITLE,
                WARNING_NOTICE_DESCRIPTION);

        InfoNotice infoNotice = new InfoNotice(TEST_FILE_NAME, INFO_NOTICE_ID, INFO_NOTICE_TITLE,
                INFO_NOTICE_DESCRIPTION);

        ErrorNotice errorNotice = new ErrorNotice(TEST_FILE_NAME, ERROR_NOTICE_ID, ERROR_NOTICE_TITLE,
                ERROR_NOTICE_DESCRIPTION);

        ValidationResultRepository underTest = new InMemoryValidationResultRepository();

        underTest.addNotice(infoNotice);
        underTest.addNotice(warningNotice);
        underTest.addNotice(errorNotice);

        assertEquals(3, underTest.getAll().size());
    }

}