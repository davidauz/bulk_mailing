package com.davidauz.blkm_send_d.test.unit;

import com.davidauz.blkm_common.entity.EmailStatusConstants;
import com.davidauz.blkm_common.repo.MailMessageRepository;
import com.davidauz.blkm_send_d.sendEmailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;

//Units are the smallest testable parts of an application.
//Here units are individually and independently tested for proper operation.
//To test units mocking operations are used so the  unit tests are run in isolation without
//affecting other parts of the application.
@SpringBootTest
@Slf4j
public class unit_tests {

}

